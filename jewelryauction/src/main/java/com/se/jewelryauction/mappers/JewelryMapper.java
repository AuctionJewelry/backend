package com.se.jewelryauction.mappers;

import com.se.jewelryauction.models.JewelryEntity;
import com.se.jewelryauction.models.JewelryMaterialEntity;
import com.se.jewelryauction.models.MaterialEntity;
import com.se.jewelryauction.requests.JewelryMaterialRequest;
import com.se.jewelryauction.requests.JewelryRequest;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface JewelryMapper {
    JewelryMapper INSTANCE = Mappers.getMapper(JewelryMapper.class);
    @Mapping(source = "brand", target = "brand.name")
    @Mapping(source = "collection", target = "collection.name")
    @Mapping(source = "category",target = "category.id")
    @Mapping(source = "materials", target = "jewelryMaterials")
    JewelryEntity toModel(JewelryRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "category",target = "category.id")
    @Mapping(source = "brand", target = "brand.name")
    @Mapping(source = "collection", target = "collection.name")
    void updateJewelryFromRequest(JewelryRequest request, @MappingTarget JewelryEntity jewelry);

    default List<JewelryMaterialEntity> mapMaterials(List<JewelryMaterialRequest> materials) {
        if (materials == null) {
            return null;
        }
        return materials.stream()
                .map(this::mapMaterial)
                .collect(Collectors.toList());
    }

    default JewelryMaterialEntity mapMaterial(JewelryMaterialRequest materialRequest) {
        if (materialRequest == null) {
            return null;
        }
        JewelryMaterialEntity jewelryMaterial = new JewelryMaterialEntity();
        MaterialEntity material = new MaterialEntity();
        material.setId(materialRequest.getIdMaterial());
        jewelryMaterial.setMaterial(material);
        jewelryMaterial.setWeight(materialRequest.getWeight());
        return jewelryMaterial;
    }





}
