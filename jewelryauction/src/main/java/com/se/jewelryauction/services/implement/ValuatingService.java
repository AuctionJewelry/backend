package com.se.jewelryauction.services.implement;

import com.se.jewelryauction.components.exceptions.AppException;
import com.se.jewelryauction.models.*;
import com.se.jewelryauction.models.enums.DeliveryStatus;
import com.se.jewelryauction.models.enums.JewelryStatus;
import com.se.jewelryauction.models.enums.ValuatingStatus;
import com.se.jewelryauction.repositories.*;
import com.se.jewelryauction.requests.MaterialsRequest;

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
    private final IDeliveryMethodRepository deliveryMethodRepository;
    private final IJewelryMaterialRepository jewelryMaterialRepository;
    private final IValuatingRepository valuatingRepository;
    private final IMaterialRepository materialRepository;
//    private final IJewelryRepository jewelryRepository;

    @Override
    public ValuatingEntity createValuating(ValuatingEntity valuating, MaterialsRequest map) {

        JewelryEntity jewelry = jewelryRepository.findById(valuating.getJewelry().getId())
                .orElseThrow(()
                        -> new AppException(HttpStatus.BAD_REQUEST, "Jewelry doesn't exist!"));
        if(valuating.isOnline()){
            float totalPrice = 0;
            List<JewelryMaterialEntity> jewelryMaterialEntities = jewelryMaterialRepository.findByJewelryId(jewelry.getId());
            for(var jerMat : jewelryMaterialEntities ){
                var quantity = map.getMaterials().get(jerMat.getMaterial().getId());
                if(quantity == null){
                    throw new AppException(HttpStatus.BAD_REQUEST, "Material does not exist");
                }
                totalPrice += jerMat.getWeight() * quantity.floatValue();
            }
            valuating.setJewelry(jewelry);
            valuating.setValuation_value(totalPrice);
            valuating.setStatus(ValuatingStatus.VALUATED);
            valuating.setValuatingFee(500000);
            valuating.setStaff(null);

        }
        else{
            UserEntity staff = userRepository.findById(valuating.getStaff().getId())
                    .orElseThrow(()
                            -> new AppException(HttpStatus.BAD_REQUEST, "User doesn't exist"));
            //Sending email to confirm about request check jewelry
            valuating.setJewelry(jewelry);
            valuating.setValuatingFee(500000);
            valuating.setStaff(staff);
            valuating.setStatus(ValuatingStatus.REQUEST);
        }
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

            existingValuating.setValuation_value(valuating.getValuation_value() != 0 ? valuating.getValuation_value() : existingValuating.getValuation_value());
            existingValuating.setStatus(valuating.getStatus() != null ? valuating.getStatus() : existingValuating.getStatus());
            existingValuating.setNotes(valuating.getNotes() != null ? valuating.getNotes() : existingValuating.getNotes());
            existingValuating.setDesiredPrice(valuating.getDesiredPrice() != 0 ? valuating.getDesiredPrice() : existingValuating.getDesiredPrice());
            existingValuating.setPaymentMethod(valuating.getPaymentMethod() != null ? valuating.getPaymentMethod() : existingValuating.getPaymentMethod());
            existingValuating.setValuatingMethod(valuating.getValuatingMethod() != null ? valuating.getValuatingMethod() : existingValuating.getValuatingMethod());
            existingValuating.setValuatingFee(valuating.getValuatingFee() != 0 ? valuating.getValuatingFee() : existingValuating.getValuatingFee());
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

    private ValuatingEntity saveValuatingAndUpdateJewelry(ValuatingEntity valuating){
        ValuatingEntity valuatingEntity = valuatingRepository.save(valuating);
        this.triggerUpdateStatusJewelry(valuating);
        this.triggerCreateDeliveryMethod(valuating);
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
                jewelry.setStatus(JewelryStatus.OFFLINE_VALUATED);
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
