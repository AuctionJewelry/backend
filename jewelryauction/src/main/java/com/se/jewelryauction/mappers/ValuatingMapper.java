package com.se.jewelryauction.mappers;

import com.se.jewelryauction.models.ValuatingEntity;
import com.se.jewelryauction.requests.CreatingValuatingRequest;
import com.se.jewelryauction.requests.UpdateValuatingRequest;
import com.se.jewelryauction.requests.ValuatingRequest;
import com.se.jewelryauction.responses.ValuatingResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ValuatingMapper {
    ValuatingMapper INSTANCE = Mappers.getMapper(ValuatingMapper.class);

    @Mapping(source = "jewelryId", target = "jewelry.id")
    @Mapping(source = "online", target = "isOnline")
    ValuatingEntity toModel(ValuatingRequest request);

    @Mapping(source = "staffId", target = "staff.id")
    ValuatingEntity toModel(UpdateValuatingRequest request);

    ValuatingResponse toResponse(ValuatingEntity valuating);
}
