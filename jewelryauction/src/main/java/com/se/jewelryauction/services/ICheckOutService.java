package com.se.jewelryauction.services;

import com.se.jewelryauction.models.DeliveryMethodEntity;
import com.se.jewelryauction.requests.CheckOutRequest;

public interface ICheckOutService {
    DeliveryMethodEntity checkOutAuction(CheckOutRequest request);
}
