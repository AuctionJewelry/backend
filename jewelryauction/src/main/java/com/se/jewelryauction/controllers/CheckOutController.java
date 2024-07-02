package com.se.jewelryauction.controllers;

import com.se.jewelryauction.components.apis.CoreApiResponse;
import com.se.jewelryauction.models.DeliveryMethodEntity;
import com.se.jewelryauction.requests.CheckOutRequest;
import com.se.jewelryauction.requests.DeliveringRequest;
import com.se.jewelryauction.services.ICheckOutService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/delivering")
    public CoreApiResponse<DeliveryMethodEntity> deliveringAuction(@RequestBody DeliveringRequest request) {
        DeliveryMethodEntity deliveryMethod = checkOutService.deliveringAuction(request);
        return CoreApiResponse.success(deliveryMethod, "Delivering auction updated successfully");
    }

    @PutMapping("/delivered/{id}")
    public CoreApiResponse<DeliveryMethodEntity> deliveredAuction(@PathVariable long id) {
        DeliveryMethodEntity deliveryMethod = checkOutService.deliveredAuction(id);
        return CoreApiResponse.success(deliveryMethod, "Delivered auction updated successfully");
    }

    @PutMapping("/confirm/{id}")
    public CoreApiResponse<DeliveryMethodEntity> confirmDelivery(@PathVariable long id) {
        DeliveryMethodEntity deliveryMethod = checkOutService.comfirmDelivery(id);
        return CoreApiResponse.success(deliveryMethod, "Delivery confirmed successfully");
    }
}
