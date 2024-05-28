package com.se.jewelryauction.controllers;

import com.se.jewelryauction.components.apis.CoreApiResponse;
import com.se.jewelryauction.models.BrandEntity;
import com.se.jewelryauction.requests.BrandRequest;
import com.se.jewelryauction.services.IBrandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.se.jewelryauction.mappers.BrandMapper.INSTANCE;

@RestController
@RequestMapping("${app.api.version.v1}/brand")
@RequiredArgsConstructor
public class BrandController {
    private final IBrandService brandService;
    @PostMapping("")
    public CoreApiResponse<BrandEntity> createMaterial(
            @Valid @RequestBody BrandRequest brandRequest
    ){
        BrandEntity birdTypeResponse = brandService.createBrand(INSTANCE.toModel(brandRequest));
        return CoreApiResponse.success(birdTypeResponse,"Insert material successfully");
    }

    @GetMapping("")
    public CoreApiResponse<List<BrandEntity>> getAllMaterials(){
        List<BrandEntity> material = brandService.getAllBrands();
        return CoreApiResponse.success(material);
    }

    @GetMapping("/{id}")
    public CoreApiResponse<BrandEntity> getMaterialById(@Valid @PathVariable Long id){
        BrandEntity brand = brandService.getBrandById(id);
        return CoreApiResponse.success(brand);
    }

    @PutMapping("/{id}")
    public CoreApiResponse<BrandEntity> updateMaterial(
            @PathVariable Long id,
            @Valid @RequestBody BrandRequest brandRequest
    ){
        BrandEntity updateBrand = brandService.updateBrand(id, INSTANCE.toModel(brandRequest));
        return CoreApiResponse.success(updateBrand, "Update brand successfully");
    }

    @DeleteMapping("/{id}")
    public CoreApiResponse<?> deleteMaterial(
            @PathVariable Long id
    ){
        brandService.deleteBrand(id);
        return CoreApiResponse.success("Delete brand successfully");
    }
}
