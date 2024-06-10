package com.se.jewelryauction.services;

import com.se.jewelryauction.models.DeliveryMethodEntity;

import java.util.List;

public interface IDeliveryMethodService {
    DeliveryMethodEntity createDeliveryMethod(DeliveryMethodEntity deliveryMethod);
    DeliveryMethodEntity updateDeliveryMethod(Long deliverMethodId, DeliveryMethodEntity deliveryMethod);
    DeliveryMethodEntity getDeliveryMethodByID(Long deliveryMethodId);
    List<DeliveryMethodEntity> getAllDeliveryMethods();
    void deleteDeliveryMethod(Long deliveryMethodId);
}
