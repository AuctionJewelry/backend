package com.se.jewelryauction.services;

import com.se.jewelryauction.models.AuctionEntity;
import com.se.jewelryauction.models.enums.AuctionStatus;
import com.se.jewelryauction.models.enums.JewelryCondition;
import com.se.jewelryauction.models.enums.Sex;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface IAuctionService {
    AuctionEntity createAuction(AuctionEntity auction) ;
    AuctionEntity getAuctionById(long id);
    List<AuctionEntity> getAllAuctions();
    AuctionEntity updateStatusAuction(long auctionId, AuctionStatus status);

    List<AuctionEntity> getAuctionsByCategoryId(Long categoryId);

    List<AuctionEntity> getAuctionsByCollectionId(Long collectionId);

    List<AuctionEntity> getMyAuctionsByStatus(AuctionStatus status);

    Page<AuctionEntity> searchAuctions(
            Long collectionId, Long categoryId, Float minPrice, Float maxPrice,
            Long brandId, JewelryCondition jewelryCondition, AuctionStatus status, Sex sex, PageRequest pageRequest);

}
