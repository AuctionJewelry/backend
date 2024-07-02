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



}
