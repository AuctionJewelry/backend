package com.se.jewelryauction.mappers;

import com.se.jewelryauction.models.BrandEntity;
import com.se.jewelryauction.requests.BrandRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
@Mapper
public interface BrandMapper {
    BrandMapper INSTANCE = Mappers.getMapper(BrandMapper.class);
    BrandEntity toModel(BrandRequest request);
}
