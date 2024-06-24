package com.se.jewelryauction.controllers;

import com.se.jewelryauction.components.apis.CoreApiResponse;
import com.se.jewelryauction.models.ValuatingEntity;

import com.se.jewelryauction.requests.UpdateValuatingRequest;
import com.se.jewelryauction.requests.ValuatingRequest;
import com.se.jewelryauction.requests.ValuatingStatusUpdateRequest;
import com.se.jewelryauction.responses.ValuatingResponse;
import com.se.jewelryauction.services.IValuatingServcie;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;

import static com.se.jewelryauction.mappers.ValuatingMapper.INSTANCE;

@RestController
@RequestMapping("${app.api.version.v1}/valuating")
@RequiredArgsConstructor
public class ValuatingController {
    private final IValuatingServcie valuatingService;

    @PostMapping("")
    public CoreApiResponse<ValuatingResponse> createValuating(
            @Valid @RequestBody ValuatingRequest valuating
    ) throws IOException, URISyntaxException {
        ValuatingResponse birdTypeResponse = valuatingService.createValuating(
                INSTANCE.toModel(valuating));
        return CoreApiResponse.success(birdTypeResponse,"Insert valuating successfully");
    }

    @GetMapping("")
    public CoreApiResponse<List<ValuatingEntity>> getAllValuating(){
        List<ValuatingEntity> Valuating = valuatingService.getAllValuating();
        return CoreApiResponse.success(Valuating);
    }

    @GetMapping("/{id}")
    public CoreApiResponse<ValuatingEntity> getValuatingById(@Valid @PathVariable Long id){
        ValuatingEntity brand = valuatingService.getValuatingById(id);
        return CoreApiResponse.success(brand);
    }

    @PutMapping("/{id}")
    public CoreApiResponse<ValuatingEntity> updateValuating(
            @PathVariable Long id,
            @Valid @RequestBody UpdateValuatingRequest request
    ){
        ValuatingEntity updateBrand = valuatingService.updateValuating(id, INSTANCE.toModel(request));
        return CoreApiResponse.success(updateBrand, "Update valuating successfully");
    }

    @PutMapping("/{id}/status")
    public CoreApiResponse<ValuatingEntity> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody ValuatingStatusUpdateRequest request
            ){
        ValuatingEntity updateBrand = valuatingService.updateStatusValuating(id, request.status);
        return CoreApiResponse.success(updateBrand, "Update valuating successfully");
    }

    @DeleteMapping("/{id}")
    public CoreApiResponse<?> deleteValuating(
            @PathVariable Long id
    ){
        valuatingService.deleteValuating(id);
        return CoreApiResponse.success("Delete valuating successfully");
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('STAFF') || hasRole('USER')")
    public CoreApiResponse<List<ValuatingEntity>> getValuatingByCurrentUser(){
        List<ValuatingEntity> valuatingEntities = valuatingService.getValuatingByCurrentUser();
        return CoreApiResponse.success(valuatingEntities);
    }


}
