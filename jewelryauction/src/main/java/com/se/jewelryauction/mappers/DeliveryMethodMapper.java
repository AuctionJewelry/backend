package com.se.jewelryauction.mappers;

import com.se.jewelryauction.models.DeliveryMethodEntity;
import com.se.jewelryauction.requests.DeliveryMethodRequest;
import com.se.jewelryauction.requests.DeliveryMethodUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DeliveryMethodMapper {
    DeliveryMethodMapper INSTANCE = Mappers.getMapper(DeliveryMethodMapper.class);

    @Mapping(source = "jewelryId", target = "jewelry.id")
    @Mapping(source = "userId", target = "user.id")
    DeliveryMethodEntity toModel(DeliveryMethodRequest deliveryMethodRequest);

    @Mapping(source = "staffId", target = "staff.id")
    DeliveryMethodEntity toModel(DeliveryMethodUpdateRequest deliveryMethodUpdateRequest);
}
