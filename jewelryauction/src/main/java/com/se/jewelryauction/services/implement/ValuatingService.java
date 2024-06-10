package com.se.jewelryauction.services.implement;

import com.se.jewelryauction.components.exceptions.AppException;
import com.se.jewelryauction.components.securities.UserPrincipal;
import com.se.jewelryauction.models.*;
import com.se.jewelryauction.models.enums.DeliveryStatus;
import com.se.jewelryauction.models.enums.JewelryStatus;
import com.se.jewelryauction.models.enums.ValuatingMethod;
import com.se.jewelryauction.models.enums.ValuatingStatus;
import com.se.jewelryauction.repositories.*;
import com.se.jewelryauction.requests.MaterialsRequest;

import com.se.jewelryauction.services.IValuatingServcie;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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
    private final IMaterialRepository materialRepository;
//    private final IJewelryRepository jewelryRepository;

    @Override
    public ValuatingEntity createValuating(ValuatingEntity valuating, List<MaterialsRequest> list) {

        JewelryEntity jewelry = jewelryRepository.findById(valuating.getJewelry().getId())
                .orElseThrow(()
                        -> new AppException(HttpStatus.BAD_REQUEST, "Jewelry doesn't exist!"));
        if(valuating.isOnline()){
            float totalPrice = 0;
            List<JewelryMaterialEntity> jewelryMaterialEntities = jewelryMaterialRepository.findByJewelryId(jewelry.getId());
            for(var jerMat : jewelryMaterialEntities ){
                float quantity = 0;
                for(var material: list){
                    if(jerMat.getMaterial().getId() == material.getMaterialID()){
                        quantity = material.getPrice();
                    }
                }
                if (quantity == 0){
                    throw new AppException(HttpStatus.BAD_REQUEST, "Can not find price of Meterial: " + jerMat.getMaterial().getName());
                }
                totalPrice += jerMat.getWeight() * quantity;
            }
            valuating.setJewelry(jewelry);
            valuating.setValuation_value(totalPrice);
            valuating.setStatus(ValuatingStatus.VALUATED);
            valuating.setValuatingFee(0);
        }
        else{
            //Sending email to confirm about request check jewelry
            valuating.setJewelry(jewelry);
            valuating.setValuatingFee(500000);
            valuating.setStatus(ValuatingStatus.REQUEST);
        }
        valuating.setStaff(null);
        return  this.saveValuatingAndUpdateJewelry(valuating);

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
        if(!existingValuating.isOnline()){
            UserEntity user;
            if (valuating.getStaff().getId() != 0){
                user = userRepository.findById(valuating.getStaff().getId())
                        .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "User doesn't exist"));
                existingValuating.setStaff(user);
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
    public void deleteValuating(long id) {
        ValuatingEntity existingValuating = this.getValuatingById(id);
        if (existingValuating != null){
            valuatingRepository.delete(existingValuating);
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

    private ValuatingEntity saveValuatingAndUpdateJewelry(ValuatingEntity valuating){
        ValuatingEntity valuatingEntity = valuatingRepository.save(valuating);
        this.triggerUpdateStatusJewelry(valuating);
        if(valuating.getStatus() == ValuatingStatus.VALUATED){
            this.triggerCreateDeliveryMethod(valuating);
        }
        return valuatingEntity;
    }

    private JewelryEntity triggerUpdateStatusJewelry(ValuatingEntity valuating){
        JewelryEntity jewelry = jewelryRepository.findById(valuating.getJewelry().getId())
                .orElseThrow(()
                        -> new AppException(HttpStatus.BAD_REQUEST, "There is no Jewelry!"));
        if(valuating.isOnline()){
            if(valuating.getStatus() == ValuatingStatus.VALUATED){
                jewelry.setStatus(JewelryStatus.ONLINE_VALUATED);
            }
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
                jewelry.setStaringPrice(valuating.getValuation_value());
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

}
