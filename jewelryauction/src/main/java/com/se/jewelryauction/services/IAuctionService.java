package com.se.jewelryauction.services;

import com.se.jewelryauction.models.AuctionEntity;
import com.se.jewelryauction.models.enums.AuctionStatus;

import java.util.List;

public interface IAuctionService {
    AuctionEntity createAuction(AuctionEntity auction) ;
    AuctionEntity getAuctionById(long id);
    List<AuctionEntity> getAllAuctions();
    AuctionEntity updateStatusAuction(long auctionId, AuctionStatus status);

}
