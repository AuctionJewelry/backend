package com.se.jewelryauction.controllers;

import com.se.jewelryauction.components.apis.CoreApiResponse;
import com.se.jewelryauction.models.ValuatingEntity;
import com.se.jewelryauction.requests.CreatingValuatingRequest;
import com.se.jewelryauction.requests.MaterialsRequest;
import com.se.jewelryauction.requests.UpdateValuatingRequest;
import com.se.jewelryauction.requests.ValuatingRequest;
import com.se.jewelryauction.services.IValuatingServcie;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.se.jewelryauction.mappers.ValuatingMapper.INSTANCE;

@RestController
@RequestMapping("${app.api.version.v1}/valuating")
@RequiredArgsConstructor
public class ValuatingController {
    private final IValuatingServcie valuatingService;

    @PostMapping("")
    public CoreApiResponse<ValuatingEntity> createValuating(
            @Valid @RequestBody CreatingValuatingRequest creatingValuatingRequest
            ){
        ValuatingEntity birdTypeResponse = valuatingService.createValuating(
                INSTANCE.toModel(creatingValuatingRequest.getRequest()),
                creatingValuatingRequest.getMaterialsRequest());
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

    @DeleteMapping("/{id}")
    public CoreApiResponse<?> deleteValuating(
            @PathVariable Long id
    ){
        valuatingService.deleteValuating(id);
        return CoreApiResponse.success("Delete valuating successfully");
    }

}
