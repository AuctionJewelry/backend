package com.se.jewelryauction.services.implement;

import com.se.jewelryauction.components.exceptions.AppException;
import com.se.jewelryauction.models.*;
import com.se.jewelryauction.models.enums.ValuatingStatus;
import com.se.jewelryauction.repositories.*;
import com.se.jewelryauction.services.IValuatingServcie;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ValuatingService implements IValuatingServcie {
    private final IUserRepository userRepository;
    private final IJewelryRepository jewelryRepository;
//    private final IDeliveryMethodRepository deliveryMethodRepository;
    private final IJewelryMaterialRepository jewelryMaterialRepository;
    private final IValuatingRepository valuatingRepository;

    @Override
    public ValuatingEntity createValuating(ValuatingEntity valuating) {
        JewelryEntity jewelry = jewelryRepository.findById(valuating.getJewelry().getId())
                .orElseThrow(()
                        -> new AppException(HttpStatus.BAD_REQUEST, "Jewelry doesn't exist!"));
        if(valuating.isOnline()){
            float totalPrice = 0;
            List<JewelryMaterialEntity> jewelryMaterialEntities = jewelryMaterialRepository.findByJewelryId(jewelry.getId());
            for(var jerMat : jewelryMaterialEntities ){
                totalPrice += jerMat.getMaterial().getPrice() * jerMat.getQuantity();
            }
            valuating.setValuation_value(totalPrice);
            valuating.setStatus(ValuatingStatus.VALUATED);
        }
        else{
            UserEntity staff = userRepository.findById(valuating.getStaff().getId())
                    .orElseThrow(()
                            -> new AppException(HttpStatus.BAD_REQUEST, "User doesn't exist"));
            //Sending email to confirm about request check jewelry
            valuating.setStatus(ValuatingStatus.REQUEST);
        }
        return  valuatingRepository.save(valuating);
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

        UserEntity user;
        if (valuating.getStaff() != null){
            user = userRepository.findById(valuating.getStaff().getId())
                    .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "User doesn't exist"));
            existingValuating.setStaff(user);
        }

        JewelryEntity jewelry = jewelryRepository.findById(valuating.getJewelry().getId())
                .orElseThrow(()
                        -> new AppException(HttpStatus.BAD_REQUEST, "Jewelry doesn't exist!"));

        if (existingValuating.getJewelry().getId() != jewelry.getId())
            throw new AppException(HttpStatus.BAD_REQUEST, "Can not change Jewelry!");

//        DeliveryMethodEntity deliveryMethod = deliveryMethodRepository.findById(valuating.getDeliveryMethod().getId())
//                .orElseThrow(()
//                        -> new AppException(HttpStatus.BAD_REQUEST, "Delivery Method doesn't exist!"));

        existingValuating.setValuation_value(valuating.getValuation_value());
        existingValuating.setStatus(valuating.getStatus());
        existingValuating.setNotes(valuating.getNotes());
//        existingValuating.setDeliveryMethod(valuating.getDeliveryMethod());
        existingValuating.setDesiredPrice(valuating.getDesiredPrice());
        existingValuating.setPaymentMethod(valuating.getPaymentMethod());

        return valuatingRepository.save(existingValuating);
    }

    @Override
    public void deleteValuating(long id) {
        ValuatingEntity existingValuating = this.getValuatingById(id);
        if (existingValuating != null){
            valuatingRepository.delete(existingValuating);
        }
    }

}
