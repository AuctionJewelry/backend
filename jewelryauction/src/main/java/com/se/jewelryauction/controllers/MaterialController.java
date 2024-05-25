package com.se.jewelryauction.controllers;
import com.se.jewelryauction.components.apis.CoreApiResponse;
import com.se.jewelryauction.models.MaterialEntity;
import com.se.jewelryauction.requests.MaterialRequest;
import com.se.jewelryauction.services.IMaterialService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.se.jewelryauction.mappers.MaterialMapper.INSTANCE;

@RestController
@RequestMapping("${app.api.version.v1}/material")
@RequiredArgsConstructor
public class MaterialController {
    private final IMaterialService materialService;
    @PostMapping("")
    public CoreApiResponse<MaterialEntity> createMaterial(
            @Valid @RequestBody MaterialRequest materialRequest
    ){
        MaterialEntity birdTypeResponse = materialService.createMaterial(INSTANCE.toModel(materialRequest));
        return CoreApiResponse.success(birdTypeResponse,"Insert material successfully");
    }

    @GetMapping("")
    public CoreApiResponse<List<MaterialEntity>> getAllMaterials(){
        List<MaterialEntity> material = materialService.getAllMaterials();
        return CoreApiResponse.success(material);
    }

    @GetMapping("/{id}")
    public CoreApiResponse<MaterialEntity> getMaterialById(@Valid @PathVariable Long id){
        MaterialEntity material = materialService.getMaterialById(id);
        return CoreApiResponse.success(material);
    }

    @PutMapping("/{id}")
    public CoreApiResponse<MaterialEntity> updateMaterial(
            @PathVariable Long id,
            @Valid @RequestBody MaterialRequest materialRequest
    ){
        MaterialEntity updateBirdType = materialService.updateMaterial(id, INSTANCE.toModel(materialRequest));
        return CoreApiResponse.success(updateBirdType, "Update material successfully");
    }

    @DeleteMapping("/{id}")
    public CoreApiResponse<?> deleteMaterial(
            @PathVariable Long id
    ){
        materialService.deleteMaterial(id);
        return CoreApiResponse.success("Delete material successfully");
    }

}
