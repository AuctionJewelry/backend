package com.se.jewelryauction.controllers;

import com.se.jewelryauction.components.apis.CoreApiResponse;
import com.se.jewelryauction.mappers.ValuatingMapper;
import com.se.jewelryauction.models.DeliveryMethodEntity;
import com.se.jewelryauction.models.ValuatingEntity;
import com.se.jewelryauction.repositories.IDeliveryMethodRepository;
import com.se.jewelryauction.requests.CreatingValuatingRequest;
import com.se.jewelryauction.requests.DeliveryMethodRequest;
import com.se.jewelryauction.requests.DeliveryMethodUpdateRequest;
import com.se.jewelryauction.requests.UpdateValuatingRequest;
import com.se.jewelryauction.services.IDeliveryMethodService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.se.jewelryauction.mappers.DeliveryMethodMapper.INSTANCE;

@RestController
@RequestMapping("${app.api.version.v1}/delivery-method")
@RequiredArgsConstructor
public class DeliveryMethodController {
    private final IDeliveryMethodService deliveryMethodService;

    @PostMapping("")
    public CoreApiResponse<DeliveryMethodEntity> createDeliveryMethod(
            @Valid @RequestBody DeliveryMethodRequest deliveryMethodRequest
    ){
        DeliveryMethodEntity deliveryMethod = deliveryMethodService
                .createDeliveryMethod(INSTANCE.toModel(deliveryMethodRequest));
        return CoreApiResponse.success(deliveryMethod,"Insert delivery method successfully");
    }

    @PutMapping("/{id}")
    public CoreApiResponse<DeliveryMethodEntity> updateDeliveryMethod(
            @PathVariable Long id,
            @Valid @RequestBody DeliveryMethodUpdateRequest deliveryMethodUpdateRequest
            ){
        DeliveryMethodEntity updateDeliveryMethod = deliveryMethodService
                .updateDeliveryMethod(id, INSTANCE.toModel(deliveryMethodUpdateRequest));
        return CoreApiResponse.success(updateDeliveryMethod, "Update valuating successfully");
    }

    @GetMapping("/{id}")
    public CoreApiResponse<DeliveryMethodEntity> getDeliveryMethodById(
            @PathVariable Long id
    ){
        DeliveryMethodEntity deliveryMethod = deliveryMethodService.getDeliveryMethodByID(id);
        return CoreApiResponse.success(deliveryMethod);
    }

    @GetMapping("")
    public CoreApiResponse<List<DeliveryMethodEntity>> getDeliveryMethodById(){
        List<DeliveryMethodEntity> deliveryMethods = deliveryMethodService.getAllDeliveryMethods();
        return CoreApiResponse.success(deliveryMethods);
    }

    @DeleteMapping("/{id}")
    public CoreApiResponse<?> deleteMethodById(
            @PathVariable Long id
    ){
        deliveryMethodService.deleteDeliveryMethod(id);
        return CoreApiResponse.success("Delete delivery method successfully!");
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('STAFF') || hasRole('USER') || hasRole('SHIPPER')")
    public CoreApiResponse<List<DeliveryMethodEntity>> getValuatingByCurrentUser(){
        List<DeliveryMethodEntity> deliveryMethodEntities = deliveryMethodService.getDeliveryMethodByCurrentUser();
        return CoreApiResponse.success(deliveryMethodEntities);
    }

}
