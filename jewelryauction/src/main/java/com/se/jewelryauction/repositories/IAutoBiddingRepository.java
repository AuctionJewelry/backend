package com.se.jewelryauction.repositories;

import com.se.jewelryauction.models.AuctionEntity;
import com.se.jewelryauction.models.AutoBiddingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IAutoBiddingRepository extends JpaRepository<AutoBiddingEntity, Long> {
    List<AutoBiddingEntity> findByAuctionId(Long auctionId);

    @Query("SELECT a FROM AutoBiddingEntity a WHERE a.auction = :auction ORDER BY a.maxBid DESC")
    AutoBiddingEntity findTop(@Param("auction") AuctionEntity auction);


}
