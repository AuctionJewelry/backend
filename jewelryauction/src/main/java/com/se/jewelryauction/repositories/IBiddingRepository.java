package com.se.jewelryauction.repositories;

import com.se.jewelryauction.models.AuctionEntity;
import com.se.jewelryauction.models.AutoBiddingEntity;
import com.se.jewelryauction.models.BiddingEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IBiddingRepository extends JpaRepository<BiddingEntity, Long> {
    @Query("SELECT b FROM BiddingEntity b WHERE b.auction.id = :auctionId")
    List<BiddingEntity> findByAuctionId(@Param("auctionId") long auctionId);




}
