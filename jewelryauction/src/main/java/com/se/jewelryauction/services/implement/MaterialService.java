package com.se.jewelryauction.services.implement;

import com.se.jewelryauction.components.exceptions.AppException;
import com.se.jewelryauction.models.MaterialEntity;
import com.se.jewelryauction.repositories.IMaterialRepository;
import com.se.jewelryauction.services.IMaterialService;
import com.se.jewelryauction.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class MaterialService implements IMaterialService {
    private final IMaterialRepository materialRepository;

    @Override
    public MaterialEntity createMaterial(MaterialEntity material) {
        material.setName(StringUtils.NameStandardlizing(material.getName()));
        if(materialRepository.existsByName(material.getName())){
            throw new AppException(HttpStatus.BAD_REQUEST, "Material name already exists");
        }
        return materialRepository.save(material);
    }

    @Override
    public MaterialEntity getMaterialById(long id) {
        return materialRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Cannot find material with id: " + id));
    }

    @Override
    public List<MaterialEntity> getAllMaterials() {
        return materialRepository.findAll();
    }

    @Override
    public MaterialEntity updateMaterial(long materialId, MaterialEntity material) {
        material.setName(StringUtils.NameStandardlizing(material.getName()));
        if(materialRepository.existsByName(material.getName())){
            throw new AppException(HttpStatus.BAD_REQUEST, "Bird type name already exists");
        }
        MaterialEntity existingMaterial = getMaterialById(materialId);
        existingMaterial.setName(material.getName());
        return materialRepository.save(existingMaterial);
    }

    @Override
    public void deleteMaterial(long id) {
        MaterialEntity existingMaterial = getMaterialById(id);
        materialRepository.deleteById(id);
    }
}
