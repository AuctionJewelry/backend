package com.se.jewelryauction.services;

import com.se.jewelryauction.models.MaterialEntity;

import java.util.List;

public interface IMaterialService {
    MaterialEntity createMaterial(MaterialEntity material) ;
    MaterialEntity getMaterialById(long id);
    List<MaterialEntity> getAllMaterials();
    MaterialEntity updateMaterial(long materialId, MaterialEntity material);
    void deleteMaterial(long id);
}
