package com.se.jewelryauction.repositories;

import com.se.jewelryauction.models.AuctionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IAuctionRepository extends JpaRepository<AuctionEntity, Long> {
    @Query("SELECT a FROM AuctionEntity a WHERE a.jewelry.category.id = :categoryId")
    List<AuctionEntity> findByCategoryId(@Param("categoryId") Long categoryId);
}
