package com.se.jewelryauction.mappers;

import com.se.jewelryauction.models.MaterialEntity;
import com.se.jewelryauction.requests.MaterialRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MaterialMapper {
    MaterialMapper INSTANCE = Mappers.getMapper(MaterialMapper.class);
    MaterialEntity toModel(MaterialRequest request);

}
