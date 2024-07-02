package com.se.jewelryauction.controllers;

import com.se.jewelryauction.components.apis.CoreApiResponse;
import com.se.jewelryauction.models.DeliveryMethodEntity;
import com.se.jewelryauction.requests.CheckOutRequest;
import com.se.jewelryauction.services.ICheckOutService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${app.api.version.v1}/checkout")
@RequiredArgsConstructor
public class CheckOutController {
    private final ICheckOutService checkOutService;
    @PostMapping("")
    public CoreApiResponse<DeliveryMethodEntity> checkOutAuction(@RequestBody CheckOutRequest request) {
        DeliveryMethodEntity deliveryMethod;
        deliveryMethod = checkOutService.checkOutAuction(request);
        return  CoreApiResponse.success(deliveryMethod,"Check out successfully");
    }
}
