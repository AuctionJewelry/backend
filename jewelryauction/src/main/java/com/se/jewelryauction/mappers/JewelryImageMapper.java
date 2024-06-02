package com.se.jewelryauction.mappers;

import com.se.jewelryauction.models.JewelryImageEntity;
import com.se.jewelryauction.responses.JewelryImageResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface JewelryImageMapper {
    JewelryImageMapper INSTANCE = Mappers.getMapper(JewelryImageMapper.class);
    JewelryImageResponse toResponse(JewelryImageEntity jewelryImage);

    @Mapping(source = "imageUrl", target = "imageUrl")
    @Mapping(source = "id", target = "id")
    List<JewelryImageResponse> toListResponse(List<JewelryImageEntity> image);
}
