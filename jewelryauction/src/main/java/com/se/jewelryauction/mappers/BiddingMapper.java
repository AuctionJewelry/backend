package com.se.jewelryauction.mappers;


import com.se.jewelryauction.models.BiddingEntity;
import com.se.jewelryauction.requests.BidRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BiddingMapper {
    BiddingMapper INSTANCE = Mappers.getMapper(BiddingMapper.class);

    @Mapping(source = "auctionId", target = "auction.id")

    BiddingEntity toModel(BidRequest request);
}
