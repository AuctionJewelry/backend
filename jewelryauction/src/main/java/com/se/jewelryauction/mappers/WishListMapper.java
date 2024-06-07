package com.se.jewelryauction.mappers;

import com.se.jewelryauction.models.WishListEntity;
import com.se.jewelryauction.requests.WishListRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
@Mapper
public interface WishListMapper {
    WishListMapper INSTANCE = Mappers.getMapper(WishListMapper.class);
    @Mapping(source = "auctionId",target = "auction.id")
    WishListEntity toModel(WishListRequest request);


}
