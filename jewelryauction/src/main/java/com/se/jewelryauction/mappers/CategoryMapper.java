package com.se.jewelryauction.mappers;

import com.se.jewelryauction.models.CategoryEntity;
import com.se.jewelryauction.requests.CategoryRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);
    CategoryEntity toModel(CategoryRequest request);
}
