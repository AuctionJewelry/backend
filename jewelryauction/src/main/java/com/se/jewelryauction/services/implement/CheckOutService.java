package com.se.jewelryauction.services.implement;

import com.se.jewelryauction.components.exceptions.AppException;
import com.se.jewelryauction.components.securities.UserPrincipal;
import com.se.jewelryauction.models.*;
import com.se.jewelryauction.models.enums.*;
import com.se.jewelryauction.repositories.*;
import com.se.jewelryauction.requests.CheckOutRequest;
import com.se.jewelryauction.requests.DeliveringRequest;
import com.se.jewelryauction.services.ICheckOutService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@AllArgsConstructor
@Service
public class CheckOutService implements ICheckOutService {
    private final IAuctionRepository auctionRepository;

    private final IWalletRepository walletRepository;

    private final IDeliveryMethodRepository deliveryMethodRepository;

    private final IJewelryRepository jewelryRepository;

    private final IUserRepository userRepository;

    private final ISystemWalletRepository systemWalletRepository;

    private final ISystemTransactionRepository transactionRepository;


    @Override
    public DeliveryMethodEntity checkOutAuction(CheckOutRequest request) {
        DeliveryMethodEntity deliveryMethod = new DeliveryMethodEntity();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UserEntity user = userPrincipal.getUser();
        deliveryMethod.setUser(user);

        AuctionEntity auction = auctionRepository.findById(request.getAuction_id())
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Can not find the auction"));
        if(!user.getId().equals(auction.getWinner().getId())){
            throw new AppException(HttpStatus.BAD_REQUEST, "You do not have access");
        }
        WalletEntity wallet = walletRepository.findByUser(user);
        wallet.setMoney(wallet.getMoney()-auction.getCurrentPrice());

        SystemTransactionEntity systemTransaction = new SystemTransactionEntity();
        systemTransaction.setSender(user);
        systemTransaction.setReceiver(auction.getJewelry().getSellerId());
        systemTransaction.setMoney(auction.getCurrentPrice());
        systemTransaction.setSystemReceive(true);
        systemTransaction.setStatus(TransactionStatus.SUBTRACTION);


        SystemWalletEntity systemWallet = new SystemWalletEntity();
        systemWallet.setAccount_balance(systemWallet.getAccount_balance()+(auction.getCurrentPrice()));
//        systemWallet.setStatus(SystemWalletStatus.ADDITION);


        if(auction.getStatus()!= AuctionStatus.Completed){
            throw new AppException(HttpStatus.BAD_REQUEST, "Unable to pay");
        }
        JewelryEntity jewelry = auction.getJewelry();
        deliveryMethod.setAddress(request.getAddress());
        deliveryMethod.setFull_name(request.getFull_name());
        deliveryMethod.setPhone_number(request.getPhone_number());
        deliveryMethod.setJewelry(jewelry);
        deliveryMethod.setStatus(DeliveryStatus.PREPARING);
        deliveryMethod.setValuatingDelivery(false);
        jewelry.setStatus(JewelryStatus.DELIVERING);
        jewelry.setPrice(auction.getCurrentPrice());
        jewelryRepository.save(jewelry);

        walletRepository.save(wallet);
        transactionRepository.save(systemTransaction);
        systemWalletRepository.save(systemWallet);



        return deliveryMethodRepository.save(deliveryMethod);
    }

    @Override
    public DeliveryMethodEntity deliveringAuction(DeliveringRequest request){
        DeliveryMethodEntity deliveryAuction = deliveryMethodRepository.findById(request.getDelivering_id())
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Can not find the delivery auction"));
        UserEntity staff = userRepository.findById(request.getStaff_id())
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Can not find the staff"));
        deliveryAuction.setStaff(staff);
        deliveryAuction.setStatus(DeliveryStatus.DELIVERING);
        return deliveryMethodRepository.save(deliveryAuction);
    }
    @Override
    public DeliveryMethodEntity deliveredAuction(long id ){
        DeliveryMethodEntity deliveryAuction = deliveryMethodRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Can not find the delivery auction"));
        deliveryAuction.setReceiving_time(LocalDateTime.now());
        deliveryAuction.setStatus(DeliveryStatus.DELIVERED);
        return deliveryMethodRepository.save(deliveryAuction);
    }
    @Override
    public DeliveryMethodEntity comfirmDelivery(long id){

        DeliveryMethodEntity deliveryAuction = deliveryMethodRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Can not find the delivery auction"));
        if(deliveryAuction.getStatus()==DeliveryStatus.RECEIVED){
            throw new AppException(HttpStatus.BAD_REQUEST, "You have confirmed your delivery ");
        }
        deliveryAuction.setStatus(DeliveryStatus.RECEIVED);
        UserEntity seller_id = deliveryAuction.getJewelry().getSellerId();

        WalletEntity wallet = walletRepository.findByUser(seller_id);
        if(wallet == null){
            wallet = new WalletEntity();
            wallet.setUser(seller_id);
            wallet.setMoney(0);
            wallet = walletRepository.save(wallet);
        }
        wallet.setMoney((float) (wallet.getMoney()+((deliveryAuction.getJewelry().getPrice())*0.95)));
        walletRepository.save(wallet);

        SystemTransactionEntity systemTransaction = new SystemTransactionEntity();
        systemTransaction.setReceiver(seller_id);
        systemTransaction.setSender(deliveryAuction.getUser());
        systemTransaction.setMoney((float) ((deliveryAuction.getJewelry().getPrice())*0.95));
        systemTransaction.setSystemSend(true);
        systemTransaction.setStatus(TransactionStatus.ADDITION);
        transactionRepository.save(systemTransaction);

        SystemWalletEntity recentWallet = systemWalletRepository.findLatestSystemWallet();

        SystemWalletEntity systemWallet = new SystemWalletEntity();
        systemWallet.setAccount_balance((float) (recentWallet.getAccount_balance()-((deliveryAuction.getJewelry().getPrice())*0.95)));
//        systemWallet.setStatus(SystemWalletStatus.SUBTRACTION);
        systemWalletRepository.save(systemWallet);

        return deliveryMethodRepository.save(deliveryAuction);
    }
    @Override
    public void paymentCheckOut(long auctionId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UserEntity user = userPrincipal.getUser();
        AuctionEntity auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Can not find the auction"));
        if(!user.getId().equals(auction.getWinner().getId())){
            throw new AppException(HttpStatus.BAD_REQUEST, "You do not have access");
        }


        WalletEntity wallet = walletRepository.findByUser(user);
        wallet.setMoney(wallet.getMoney()-auction.getCurrentPrice());
        walletRepository.save(wallet);

        SystemTransactionEntity systemTransaction = new SystemTransactionEntity();
        systemTransaction.setSender(user);
        systemTransaction.setMoney(auction.getCurrentPrice());
        systemTransaction.setSystemReceive(true);
        transactionRepository.save(systemTransaction);

        SystemWalletEntity systemWallet = new SystemWalletEntity();
        systemWallet.setAccount_balance(systemWallet.getAccount_balance()+(auction.getCurrentPrice()));
//        systemWallet.setStatus(SystemWalletStatus.ADDITION);
        systemWalletRepository.save(systemWallet);




    }

}

