package com.se.jewelryauction.services.implement;

import com.se.jewelryauction.components.exceptions.AppException;
import com.se.jewelryauction.components.securities.UserPrincipal;
import com.se.jewelryauction.models.*;
import com.se.jewelryauction.models.enums.AuctionStatus;
import com.se.jewelryauction.models.enums.DeliveryStatus;
import com.se.jewelryauction.models.enums.JewelryStatus;
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

        if(auction.getStatus()!= AuctionStatus.Completed){
            throw new AppException(HttpStatus.BAD_REQUEST, "Unable to pay");
        }
        JewelryEntity jewelry = auction.getJewelry();
        deliveryMethod.setAddress(request.getAddress());
        deliveryMethod.setFull_name(request.getFull_name());
        deliveryMethod.setPhone_number(request.getPhone_number());
        deliveryMethod.setJewelry(jewelry);
        deliveryMethod.setStatus(DeliveryStatus.PREPARING);
        deliveryMethod.setValuatingDelivery(true);
        jewelry.setStatus(JewelryStatus.DELIVERING);
        jewelry.setPrice(auction.getCurrentPrice());
        jewelryRepository.save(jewelry);


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
        deliveryAuction.setStatus(DeliveryStatus.RECEIVED);
        return deliveryMethodRepository.save(deliveryAuction);
    }

}
