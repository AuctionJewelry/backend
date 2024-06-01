package com.se.jewelryauction.mappers;

import com.se.jewelryauction.models.CollectionEntity;
import com.se.jewelryauction.requests.CollectionRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CollectionMapper {
    CollectionMapper INSTANCE = Mappers.getMapper(CollectionMapper.class);

    @Mapping(source = "brand", target = "brand.name")
    CollectionEntity toModel(CollectionRequest collectionRequest);
}
