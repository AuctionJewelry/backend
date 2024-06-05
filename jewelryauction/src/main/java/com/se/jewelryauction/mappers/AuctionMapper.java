package com.se.jewelryauction.mappers;

import com.se.jewelryauction.models.AuctionEntity;
import com.se.jewelryauction.requests.AuctionRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper

public interface AuctionMapper {
    AuctionMapper INSTANCE = Mappers.getMapper(AuctionMapper.class);
    @Mapping(source = "jewelryId", target = "jewelry.id")
    AuctionEntity toModel(AuctionRequest request);
}
