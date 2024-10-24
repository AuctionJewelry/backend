package com.se.jewelryauction.services.implement;

import com.se.jewelryauction.components.exceptions.AppException;
import com.se.jewelryauction.components.securities.UserPrincipal;
import com.se.jewelryauction.mappers.ValuatingMapper;
import com.se.jewelryauction.models.*;
import com.se.jewelryauction.models.enums.DeliveryStatus;
import com.se.jewelryauction.models.enums.JewelryStatus;
import com.se.jewelryauction.models.enums.ValuatingMethod;
import com.se.jewelryauction.models.enums.ValuatingStatus;
import com.se.jewelryauction.repositories.*;
import com.se.jewelryauction.responses.PaymentResponse;
import com.se.jewelryauction.responses.ValuatingPerMaterialResponse;
import com.se.jewelryauction.responses.ValuatingResponse;
import com.se.jewelryauction.responses.ValuatingStaffResponse;
import com.se.jewelryauction.services.IPaymentService;
import com.se.jewelryauction.services.IValuatingServcie;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ValuatingService implements IValuatingServcie {
    private final IUserRepository userRepository;
    private final IJewelryRepository jewelryRepository;
    private final IDeliveryMethodRepository deliveryMethodRepository;
    private final IJewelryMaterialRepository jewelryMaterialRepository;
    private final IValuatingRepository valuatingRepository;
    private final IPaymentService paymentService;
    private final IWalletRepository walletRepository;

    public final ISystemWalletRepository systemWalletRepository;
    public final ISystemTransactionRepository systemTransactionRepository;
    @Override
    public ValuatingResponse createValuating(ValuatingEntity valuating) throws IOException, URISyntaxException {

        JewelryEntity jewelry = jewelryRepository.findById(valuating.getJewelry().getId())
                .orElseThrow(()
                        -> new AppException(HttpStatus.BAD_REQUEST, "Jewelry doesn't exist!"));
        List<ValuatingPerMaterialResponse> perMaterialResponses = new ArrayList<>();
        if(valuating.isOnline()){
            float totalPrice = 0;
            List<JewelryMaterialEntity> jewelryMaterialEntities = jewelryMaterialRepository.findByJewelryId(jewelry.getId());
            for(var jerMat : jewelryMaterialEntities ){
                ValuatingPerMaterialResponse materialResponse =
                        new ValuatingPerMaterialResponse(jerMat.getMaterial(), jerMat.getWeight(), this.getCurrentPrice(jerMat.getMaterial().getName()));
                perMaterialResponses.add(materialResponse);
                totalPrice += materialResponse.getSum();
            }
            valuating.setJewelry(jewelry);
            valuating.setValuation_value(totalPrice);
            valuating.setStatus(ValuatingStatus.VALUATED);
            valuating.setValuatingFee(0);
        }
        else{
            List<ValuatingEntity> existingValuating = valuatingRepository.findByJewelryId(valuating.getJewelry().getId());
            boolean isAvailableCreate = true;
            for(var valuate: existingValuating){
                if(!(valuate.getStatus().equals(ValuatingStatus.VALUATED) ||
                                valuate.getStatus().equals(ValuatingStatus.REJECTED))){
                    isAvailableCreate = false;
                    break;
                }
            }
            if(!isAvailableCreate){
                throw new AppException(HttpStatus.BAD_REQUEST, "Jewelry in the offline valuating!");
            }
            //Sending email to confirm about request check jewelry
            valuating.setJewelry(jewelry);
            valuating.setValuatingFee(500000);
            valuating.setStatus(ValuatingStatus.REQUEST);
//            valuating.setNotes("Revaluating");
        }
        valuating.setStaff(null);
        if(valuating.isOnline()){
            ValuatingResponse valuatingResponse = ValuatingMapper.INSTANCE.toResponse(this.saveValuatingAndUpdateJewelry(valuating));
            valuatingResponse.setMaterialPriceResponse(perMaterialResponses);
            return valuatingResponse;
        }
        checkWallet();
        valuating = this.saveValuatingAndUpdateJewelry(valuating);
        ValuatingResponse valuatingResponse = ValuatingMapper.INSTANCE.toResponse(valuating);
        valuatingResponse.setPaymentResponse("Success");
        return valuatingResponse;
    }

    private void checkWallet(){
        WalletEntity wallet = walletRepository.findByUser(this.getCurrentUser());
        if(wallet.getMoney() >= 500000){
            List<SystemWalletEntity> systemWalletEntities = systemWalletRepository.findAll();
            SystemWalletEntity systemWallet;
            if(systemWalletEntities.size() == 0){
                systemWallet = new SystemWalletEntity();
                systemWallet.setAccount_balance(0);

                systemWalletRepository.save(systemWallet);
            }
            else{
                systemWallet = systemWalletEntities.get(0);
            }
            SystemTransactionEntity systemTransactionEntity = new SystemTransactionEntity();
            systemTransactionEntity.setSystemReceive(true);
            systemTransactionEntity.setSender(wallet.getUser());
            systemTransactionEntity.setMoney(500000);

            systemTransactionRepository.save(systemTransactionEntity);

            wallet.setMoney(wallet.getMoney() - 500000);
            systemWallet.setAccount_balance(systemWallet.getAccount_balance() + 500000);
            walletRepository.save(wallet);
            systemWalletRepository.save(systemWallet);
        }
        else{
            throw new AppException(HttpStatus.BAD_REQUEST, "You don't have enough money!");
        }
    }

    @Override
    public ValuatingEntity getValuatingById(long id) {
        ValuatingEntity existingValuating = valuatingRepository.findById(id)
                .orElseThrow(()
                        -> new AppException(HttpStatus.BAD_REQUEST, "This valuating does not exist!"));
        return existingValuating;
    }

    @Override
    public List<ValuatingEntity> getAllValuating() {
        return valuatingRepository.findAll();
    }

    @Override
    public ValuatingEntity updateValuating(long valuatingId, ValuatingEntity valuating) {
        ValuatingEntity existingValuating = this.getValuatingById(valuatingId);
        JewelryEntity jewelry = existingValuating.getJewelry();
        if(!existingValuating.isOnline()){
            UserEntity user;
            if(valuating.getStaff() != null){
                if (valuating.getStaff().getId() != 0){
                    user = userRepository.findById(valuating.getStaff().getId())
                            .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "User doesn't exist"));
                    if(user.getRole_id().getId() == 3){
                        List<ValuatingEntity> valuatingEntities = valuatingRepository.findByStatusAndStaffId(ValuatingStatus.PREPARING, user.getId());
                        if(valuatingEntities.size() < 5){
                            existingValuating.setStaff(user);
                            existingValuating.setStatus(ValuatingStatus.PREPARING);
                        }
                        else{
                            throw new AppException(HttpStatus.BAD_REQUEST, "User is busy!");
                        }
                    }
                    else{
                        throw new AppException(HttpStatus.BAD_REQUEST, "User is not a staff!");
                    }
                }
            }

            existingValuating.setAddress(
                    (valuating.getAddress() != null)
                            ? valuating.getAddress()
                            : existingValuating.getAddress());

            existingValuating.setValuation_value(
                    valuating.getValuation_value() != 0
                            ? valuating.getValuation_value()
                            : existingValuating.getValuation_value());

            existingValuating.setStatus(
                    valuating.getStatus() != null
                            ? valuating.getStatus()
                            : existingValuating.getStatus());

            existingValuating.setNotes(
                    (valuating.getNotes() != null)
                            ? valuating.getNotes()
                            : existingValuating.getNotes());

            existingValuating.setDesiredPrice(
                    valuating.getDesiredPrice() != 0
                            ? valuating.getDesiredPrice()
                            : existingValuating.getDesiredPrice());

            existingValuating.setStartingPrice(
                    valuating.getStartingPrice() != 0
                            ? valuating.getStartingPrice()
                            : existingValuating.getStartingPrice());


            existingValuating.setPaymentMethod(
                    valuating.getPaymentMethod() != null
                            ? valuating.getPaymentMethod()
                            : existingValuating.getPaymentMethod());

            existingValuating.setValuatingMethod(
                    valuating.getValuatingMethod() != null
                            ? valuating.getValuatingMethod()
                            : existingValuating.getValuatingMethod());
        }
        return this.saveValuatingAndUpdateJewelry(existingValuating);

    }

    @Override
    public ValuatingEntity updateStatusValuating(long valuatingId, ValuatingStatus valuatingStatus) {
        ValuatingEntity valuatingEntity = new ValuatingEntity();
        valuatingEntity.setStatus(valuatingStatus);
        return this.updateValuating(valuatingId, valuatingEntity);
    }


    @Override
    public void deleteValuating(long id) {
        ValuatingEntity existingValuating = this.getValuatingById(id);
        if (existingValuating != null){
            this.updateStatusValuating(id, ValuatingStatus.REJECTED);
            this.refundMoney(id);
        }
    }

    @Override
    public List<ValuatingEntity> getValuatingByJewelryId(long id) {
        List<ValuatingEntity> valuatingEntities = valuatingRepository.findByJewelryId(id);
        return valuatingEntities;
    }

    @Override
    public List<ValuatingEntity> getValuatingByCurrentUser() {
        List<ValuatingEntity> valuatingEntities = new ArrayList<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UserEntity user = userPrincipal.getUser();
        if(user.getRole_id().getName().equalsIgnoreCase("staff")){
            valuatingEntities = valuatingRepository.findByStaffId(user.getId());
        }
        else{
            valuatingEntities = valuatingRepository.findByJewelrySellerId(user);
        }
        return valuatingEntities;
    }

    public float getCurrentPrice(String material) throws IOException, URISyntaxException {
        try {
            if (material.equalsIgnoreCase("diamond")){
//                String API_URL = "https://api.idexonline.com/realtimeprices/api/getprice";
//                String API_KEY = "ABNSSA1223311";
//                String API_SECRET = "SeCKeccdaaedf123";
//                // Construct the URL with query parameters
//                String urlString = "https://api.metals.dev/v1/latest"
//                        + "?shape=" + "Round"
//                        + "&weight=" + "1"
//                        + "&color=" + "E"
//                        + "&clarity=" + "VVS1"
//                        + "&cut_grade=" + ""
//                        + "&grading_lab=" + "GIA";
//                URL url = new URL(API_URL + urlString);
//                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                connection.setRequestMethod("GET");
//                connection.setRequestProperty("api_key", API_KEY);
//                connection.setRequestProperty("api_secret", API_SECRET);
//                int responseCode = connection.getResponseCode();
//                if (responseCode == HttpURLConnection.HTTP_OK) {
//                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//                    String inputLine;
//                    StringBuilder response = new StringBuilder();
//
//                    while ((inputLine = in.readLine()) != null) {
//                        response.append(inputLine);
//                    }
//                    in.close();
//
//                    String responseString = response.toString();
//                    String[] list = responseString.split("[:\"{},] \\s\\n");
//                    int index = -1;
//                    for (int i = 0; i < list.length; i++){
//                        if(list[i].equalsIgnoreCase("avg_price_per_carat")){
//                            index = i;
//                            break;
//                        }
//                    }
//                    if (index != -1){
//                        return Float.parseFloat(list[index + 1]);
//                    }
//                } else {
//                    System.out.println("GET request failed. Response Code: " + responseCode);
//                }
                return 235597550f;
            }
            else{
                // Define the API endpoint and query parameters
                String apiKey = "YLCJ9X2IRXSGAO8J8HDZ2218J8HDZ";
                String currency = "VND";
                String unit = "g";
                String urlString = "https://api.metals.dev/v1/latest"
                        + "?api_key=" + apiKey
                        + "&currency=" + currency
                        + "&unit=" + unit;

                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept", "application/json");
                int responseCode = connection.getResponseCode();
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                String responseString = response.toString();
                String[] list = responseString.split("[,:\"{}\\s\\n]+");
                int index = -1;
                for (int i = 0; i < list.length; i++){
                    if(list[i].equalsIgnoreCase(material)){
                        index = i;
                        break;
                    }
                }
                if (index != -1){
                    return Float.parseFloat(list[index + 1]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<ValuatingStaffResponse> getValuatingStaff() {
        List<UserEntity> userList = userRepository.findByRoleId_Id(3L);
        List<ValuatingStaffResponse> valuatingStaffResponses = new ArrayList<>();
        for(var user : userList){
            List<ValuatingEntity> valuatingPreparingEntities = valuatingRepository.findByStatusAndStaffId(ValuatingStatus.PREPARING, user.getId());
            if(valuatingPreparingEntities.size() < 5){
                valuatingStaffResponses
                        .add(new ValuatingStaffResponse (user, valuatingPreparingEntities.size()));
            }
        }
        return valuatingStaffResponses;
    }

    @Override
    public ValuatingResponse reValuating(long id) {
        ValuatingEntity valuating = this.getValuatingById(id);
        boolean isAvailableCreate = true;
        if(!(valuating.getStatus().equals(ValuatingStatus.VALUATED) ||
                valuating.getStatus().equals(ValuatingStatus.REJECTED))){
            isAvailableCreate = false;
        }
        if(!isAvailableCreate){
            throw new AppException(HttpStatus.BAD_REQUEST, "Jewelry in the offline valuating!");
        }

        //Sending email to confirm about request check jewelry
        valuating.setValuatingFee(500000);
        valuating.setStatus(ValuatingStatus.REQUEST);
        valuating.setStaff(null);

        //Check wallet
        checkWallet();
        valuating = this.updateValuating(id, valuating);
        ValuatingResponse valuatingResponse = ValuatingMapper.INSTANCE.toResponse(valuating);
        valuatingResponse.setPaymentResponse("Success");
        return valuatingResponse;
    }

    private ValuatingEntity saveValuatingAndUpdateJewelry(ValuatingEntity valuating){
        if(!valuating.isOnline()){
            valuating = valuatingRepository.save(valuating);
        }
        this.triggerUpdateStatusJewelry(valuating);
        if(valuating.getStatus() == ValuatingStatus.VALUATED && !valuating.isOnline()){
            this.triggerCreateDeliveryMethod(valuating);
        }
        return valuating;
    }

    private JewelryEntity triggerUpdateStatusJewelry(ValuatingEntity valuating){
        JewelryEntity jewelry = jewelryRepository.findById(valuating.getJewelry().getId())
                .orElseThrow(()
                        -> new AppException(HttpStatus.BAD_REQUEST, "There is no Jewelry!"));
        if(valuating.isOnline()){
//            if(valuating.getStatus() == ValuatingStatus.VALUATED){
//                if(valuatingRepository.findByJewelryId(jewelry.getId()) == null){
//                    jewelry.setStatus(JewelryStatus.ONLINE_VALUATED);
//                }
//            }
        }
        else{
            if(valuating.getStatus() == ValuatingStatus.REQUEST){
                jewelry.setStatus(JewelryStatus.OFFLINE_VALUATING);
            }
            else if (valuating.getStatus() == ValuatingStatus.VALUATED){
                if(valuating.getValuatingMethod() == ValuatingMethod.AT_HOME_VALUATION)
                    jewelry.setStatus(JewelryStatus.VALUATING_DELIVERING);
                if(valuating.getValuatingMethod() == ValuatingMethod.DIRECTLY_VALUATION)
                    jewelry.setStatus(JewelryStatus.STORED);
                jewelry.setStaringPrice(valuating.getStartingPrice());
            }
        }
        return jewelryRepository.save(jewelry);
    }

    private DeliveryMethodEntity triggerCreateDeliveryMethod(ValuatingEntity valuating){
        JewelryEntity jewelry = jewelryRepository.findById(valuating.getJewelry().getId())
                .orElseThrow(()
                        -> new AppException(HttpStatus.BAD_REQUEST, "There is no Jewelry!"));
        DeliveryMethodEntity deliveryMethod = new DeliveryMethodEntity();
        if(!valuating.isOnline()){
            if (valuating.getStatus() == ValuatingStatus.VALUATED){
                deliveryMethod.setValuatingDelivery(true);
                deliveryMethod.setUser(jewelry.getSellerId());
                deliveryMethod.setJewelry(jewelry);
                deliveryMethod.setAddress("Cty TNHH Jewelry Auction");
                deliveryMethod.setStaff(valuating.getStaff());
                deliveryMethod.setFull_name("Cty TNHH Jewelry Auction");
                deliveryMethod.setStatus(DeliveryStatus.DELIVERING);
            }
        }
        return deliveryMethodRepository.save(deliveryMethod);
    }

    private UserEntity getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UserEntity user = userPrincipal.getUser();
        return user;
    }

    private boolean refundMoney(long valuatingId){
        ValuatingEntity valuating = this.getValuatingById(valuatingId);

        List<SystemWalletEntity> systemWalletEntities = systemWalletRepository.findAll();
        SystemWalletEntity systemWallet;
        if(systemWalletEntities.size() == 0){
            systemWallet = new SystemWalletEntity();
            systemWallet.setAccount_balance(0);

            systemWalletRepository.save(systemWallet);
        }
        else{
            systemWallet = systemWalletEntities.get(0);
        }

        if(systemWallet.getAccount_balance() >= 500000){
            WalletEntity wallet = walletRepository.findByUser(valuating.getJewelry().getSellerId());
            if(wallet == null){
                WalletEntity walletEntity = new WalletEntity();
                walletEntity.setUser(valuating.getJewelry().getSellerId());
                walletEntity.setMoney(0);
                wallet = walletRepository.save(walletEntity);
            }

            SystemTransactionEntity systemTransactionEntity = new SystemTransactionEntity();
            systemTransactionEntity.setSystemSend(true);
            systemTransactionEntity.setSystemReceive(false);
            systemTransactionEntity.setReceiver(wallet.getUser());
            systemTransactionEntity.setMoney(500000);

            systemTransactionRepository.save(systemTransactionEntity);

            wallet.setMoney(wallet.getMoney() + 500000);
            systemWallet.setAccount_balance(systemWallet.getAccount_balance() - 500000);
            walletRepository.save(wallet);
            systemWalletRepository.save(systemWallet);
        }

        else{
            throw new AppException(HttpStatus.BAD_REQUEST, "You don't have enough money to refund!");
        }

        return true;
    }

}
