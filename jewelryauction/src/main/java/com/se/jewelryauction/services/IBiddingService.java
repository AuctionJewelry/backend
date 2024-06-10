package com.se.jewelryauction.services;

import com.se.jewelryauction.models.AuctionEntity;
import com.se.jewelryauction.models.BiddingEntity;
import com.se.jewelryauction.requests.BidRequest;

import java.util.List;

public interface IBiddingService {
    void createBidding(BidRequest bidRequest) ;
    List<BiddingEntity> getBiddingByAuctionId(long id);
}
