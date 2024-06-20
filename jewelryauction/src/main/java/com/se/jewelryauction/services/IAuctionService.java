package com.se.jewelryauction.services;

import com.se.jewelryauction.models.AuctionEntity;
import com.se.jewelryauction.models.enums.AuctionStatus;
import com.se.jewelryauction.models.enums.JewelryCondition;
import com.se.jewelryauction.models.enums.Sex;
import com.se.jewelryauction.requests.UpdateTimeAuctionRequest;
import com.se.jewelryauction.responses.AuctionResponse;
import com.se.jewelryauction.responses.ListBidForAuction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface IAuctionService {
    AuctionEntity createAuction(AuctionEntity auction) ;
    AuctionEntity getAuctionById(long id);
    List<AuctionEntity> getAllAuctions();

    List<AuctionEntity> getAuctionsByCategoryId(Long categoryId);

    List<AuctionEntity> getAuctionsByCollectionId(Long collectionId);

    List<AuctionEntity> getMyAuctionsByStatus(AuctionStatus status);

    Page<AuctionEntity> searchAuctions(
            Long collectionId, Long categoryId, Float minPrice, Float maxPrice,
            Long brandId, JewelryCondition jewelryCondition, AuctionStatus status, Sex sex, PageRequest pageRequest);

    void cancelAuction(long id);

    AuctionEntity updateTime(Long auctionId, UpdateTimeAuctionRequest request);


    List<AuctionResponse> getAuctionsByUserId();
    List<ListBidForAuction> getBidsByAuctionId(Long auctionId);

    List<AuctionEntity> getAuctionsWin();
}
