package com.se.jewelryauction.services.implement;

import com.se.jewelryauction.components.exceptions.AppException;
import com.se.jewelryauction.models.DeliveryMethodEntity;
import com.se.jewelryauction.models.JewelryEntity;
import com.se.jewelryauction.models.UserEntity;
import com.se.jewelryauction.models.ValuatingEntity;
import com.se.jewelryauction.models.enums.DeliveryStatus;
import com.se.jewelryauction.models.enums.JewelryStatus;
import com.se.jewelryauction.repositories.IDeliveryMethodRepository;
import com.se.jewelryauction.repositories.IJewelryRepository;
import com.se.jewelryauction.repositories.IUserRepository;
import com.se.jewelryauction.repositories.IValuatingRepository;
import com.se.jewelryauction.services.IDeliveryMethodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryMethodService implements IDeliveryMethodService {
    private final IDeliveryMethodRepository deliveryMethodRepository;
    private final IUserRepository userRepository;
    private final IJewelryRepository jewelryRepository;
    private final IValuatingRepository valuatingRepository;

    @Override
    public DeliveryMethodEntity createDeliveryMethod(DeliveryMethodEntity deliveryMethod) {
        UserEntity user = userRepository.findById(deliveryMethod.getUser().getId())
                .orElseThrow(()
                        -> new AppException(HttpStatus.BAD_REQUEST, "Can not find the buyer"));
        deliveryMethod.setUser(user);

        JewelryEntity jewelry = jewelryRepository.findById(deliveryMethod.getJewelry().getId())
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Can not find the jewelry"));
        deliveryMethod.setJewelry(jewelry);

        deliveryMethod.setStatus(DeliveryStatus.PREPARING);
        deliveryMethod.setStaff(findTheStaffOfValuating(jewelry));
        deliveryMethod.setValuatingDelivery(false);
        return this.saveDeliveryMethodAndTrigger(deliveryMethod);
    }

    @Override
    public DeliveryMethodEntity updateDeliveryMethod(Long deliverMethodId, DeliveryMethodEntity deliveryMethod) {
        DeliveryMethodEntity existingDeliverMethod = this.getDeliveryMethodByID(deliverMethodId);
//        if(existingDeliverMethod.getStatus() == DeliveryStatus.DELIVERING
//                || existingDeliverMethod.getStatus() == DeliveryStatus.DELIVERED){
//            throw new AppException(HttpStatus.BAD_REQUEST, "Can not change status because of delivering");
//        }

        if(deliveryMethod.getStaff().getId() != 0){
            UserEntity user = userRepository.findById(deliveryMethod.getStaff().getId())
                    .orElseThrow(()
                            -> new AppException(HttpStatus.BAD_REQUEST, "User doesn't exist"));
            if(user.getRole_id().getName().equalsIgnoreCase("Staff")){
                existingDeliverMethod.setStaff(user);
            }
            else{
                throw new AppException(HttpStatus.BAD_REQUEST, "User doesn't Staff");
            }
        }

        existingDeliverMethod.setFull_name(
                deliveryMethod.getFull_name() != null
                        ? deliveryMethod.getFull_name()
                        : existingDeliverMethod.getFull_name());

        existingDeliverMethod.setPhone_number(
                deliveryMethod.getPhone_number() != null
                        ? deliveryMethod.getPhone_number()
                        : existingDeliverMethod.getPhone_number());

        existingDeliverMethod.setAddress(
                deliveryMethod.getAddress() != null
                        ? deliveryMethod.getAddress()
                        : existingDeliverMethod.getAddress());

        existingDeliverMethod.setStatus(
                deliveryMethod.getStatus() != null
                        ? deliveryMethod.getStatus()
                        : existingDeliverMethod.getStatus());

        return this.saveDeliveryMethodAndTrigger(existingDeliverMethod);
    }

    @Override
    public DeliveryMethodEntity getDeliveryMethodByID(Long deliveryMethodId) {
        DeliveryMethodEntity existingDeliverMethod = deliveryMethodRepository.findById(deliveryMethodId)
                .orElseThrow(()
                        -> new AppException(HttpStatus.BAD_REQUEST, "Can not find Delivery Method!"));
        return existingDeliverMethod;
    }

    @Override
    public List<DeliveryMethodEntity> getAllDeliveryMethods() {
        return deliveryMethodRepository.findAll();
    }

    @Override
    public void deleteDeliveryMethod(Long deliveryMethodId) {
        DeliveryMethodEntity existingDeliverMethod = this.getDeliveryMethodByID(deliveryMethodId);
        if(existingDeliverMethod != null){
            deliveryMethodRepository.delete(existingDeliverMethod);
        }
    }

    private DeliveryMethodEntity saveDeliveryMethodAndTrigger(DeliveryMethodEntity deliveryMethod){
        DeliveryMethodEntity deliveryMethod1 = deliveryMethodRepository.save(deliveryMethod);
        this.triggerJewelryStatus(deliveryMethod1);
        return deliveryMethod1;
    }

    private void triggerJewelryStatus(DeliveryMethodEntity deliveryMethod){
        JewelryEntity jewelry = jewelryRepository.findById(deliveryMethod.getJewelry().getId())
                .orElseThrow(()
                        -> new AppException(HttpStatus.BAD_REQUEST, "There are no jewelry!"));
        if(deliveryMethod.isValuatingDelivery() ){
            if(deliveryMethod.getStatus() == DeliveryStatus.DELIVERING){
                jewelry.setStatus(JewelryStatus.VALUATING_DELIVERING);
            }
            else if (deliveryMethod.getStatus() == DeliveryStatus.DELIVERED){
                jewelry.setStatus(JewelryStatus.STORED);
            }
        }
        else{
            if(deliveryMethod.getStatus() == DeliveryStatus.DELIVERING){
                jewelry.setStatus(JewelryStatus.DELIVERING);
            }
            else if (deliveryMethod.getStatus() == DeliveryStatus.DELIVERED){
                jewelry.setStatus(JewelryStatus.DELIVERED);
            }
        }
        jewelryRepository.save(jewelry);
    }

    private UserEntity findTheStaffOfValuating(JewelryEntity jewelry){
        List<ValuatingEntity> valuatingEntities = valuatingRepository.findByJewelryId(jewelry.getId());
        Long maxId = valuatingEntities.get(0).getId();
        UserEntity staff = valuatingEntities.get(0).getStaff();
        for (int i = 0; i < valuatingEntities.size(); i++){
            ValuatingEntity valuating = valuatingEntities.get(i);
            if(maxId < valuating.getId()){
                staff = valuating.getStaff();
            }
        }
        return staff;
    }
}
