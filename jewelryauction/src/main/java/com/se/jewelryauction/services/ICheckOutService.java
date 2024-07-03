package com.se.jewelryauction.services;

import com.se.jewelryauction.models.DeliveryMethodEntity;
import com.se.jewelryauction.requests.CheckOutRequest;
import com.se.jewelryauction.requests.DeliveringRequest;

public interface ICheckOutService {
    DeliveryMethodEntity checkOutAuction(CheckOutRequest request);

    DeliveryMethodEntity deliveringAuction(DeliveringRequest request);
    DeliveryMethodEntity deliveredAuction(long id );

    DeliveryMethodEntity comfirmDelivery(long id);

    void paymentCheckOut(long auctionId);
}
