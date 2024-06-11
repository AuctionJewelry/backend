package com.se.jewelryauction.mappers;

import com.se.jewelryauction.models.AuctionEntity;
import com.se.jewelryauction.models.JewelryEntity;
import com.se.jewelryauction.requests.AuctionRequest;
import com.se.jewelryauction.requests.JewelryRequest;
import com.se.jewelryauction.requests.UpdateTimeAuctionRequest;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AuctionMapper {
    AuctionMapper INSTANCE = Mappers.getMapper(AuctionMapper.class);
    @Mapping(source = "jewelryId", target = "jewelry.id")
    AuctionEntity toModel(AuctionRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAuctionFromRequest(UpdateTimeAuctionRequest request, @MappingTarget AuctionEntity auction);
}
