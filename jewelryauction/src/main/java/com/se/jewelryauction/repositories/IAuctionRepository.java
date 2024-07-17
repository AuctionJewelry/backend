package com.se.jewelryauction.repositories;

import com.se.jewelryauction.models.AuctionEntity;
import com.se.jewelryauction.models.UserEntity;
import com.se.jewelryauction.models.enums.AuctionStatus;
import com.se.jewelryauction.models.enums.JewelryCondition;
import com.se.jewelryauction.models.enums.Sex;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IAuctionRepository extends JpaRepository<AuctionEntity, Long> {


    @Query("SELECT a FROM AuctionEntity a WHERE a.jewelry.category.id = :categoryId AND a.status = 'INPROGRESS'")
    List<AuctionEntity> findByCategoryId(@Param("categoryId") Long categoryId);

    @Query("SELECT a FROM AuctionEntity a WHERE a.jewelry.collection.id = :collectionId AND a.status = 'INPROGRESS'")
    List<AuctionEntity> findByCollectionId(@Param("collectionId") Long collectionId);

    @Query("SELECT a FROM AuctionEntity a WHERE a.jewelry.brand.id = :brandId AND a.status = 'INPROGRESS'")
    List<AuctionEntity> findByBrandId(@Param("brandId") Long brandId);


    @Query("SELECT a FROM AuctionEntity a WHERE a.jewelry.sex = :sex AND a.status = 'INPROGRESS'")
    List<AuctionEntity> findByGenderAndStatus(@Param("sex") Sex sex);

    @Query("SELECT a FROM AuctionEntity a WHERE a.jewelry.sellerId.id = :sellerId " +
            "AND (:status IS NULL OR a.status = :status)")
    List<AuctionEntity> findBySellerIdAndStatus(@Param("sellerId") Long sellerId, @Param("status") AuctionStatus status);

    @Query("SELECT a FROM AuctionEntity a WHERE " +
            "(:collectionId IS NULL OR a.jewelry.collection.id = :collectionId) AND " +
            "(:categoryId IS NULL OR a.jewelry.category.id = :categoryId) AND " +
            "(:minPrice IS NULL OR a.currentPrice >= :minPrice) AND " +
            "(:maxPrice IS NULL OR a.currentPrice <= :maxPrice) AND " +
            "(:brandId IS NULL OR a.jewelry.brand.id = :brandId) AND " +
            "(:jewelryCondition IS NULL OR a.jewelry.jewelryCondition = :jewelryCondition) AND " +
            "(:status IS NULL OR a.status = :status) AND " +
            "(:sex IS NULL OR a.jewelry.sex = :sex)")
    Page<AuctionEntity> searchAuctions(
            @Param("collectionId") Long collectionId,
            @Param("categoryId") Long categoryId,
            @Param("minPrice") Float minPrice,
            @Param("maxPrice") Float maxPrice,
            @Param("brandId") Long brandId,
            @Param("jewelryCondition") JewelryCondition jewelryCondition,
            @Param("status") AuctionStatus status,
            @Param("sex") Sex sex,
            Pageable pageable);

    @Query("SELECT a FROM AuctionEntity a WHERE a.jewelry.id = :jewelryId AND a.status NOT IN ('FAIL', 'CANCEL')")
    List<AuctionEntity> findActiveAuctionsByJewelryId(Long jewelryId);
    List<AuctionEntity> findByWinnerAndStatus(UserEntity winner, AuctionStatus status);

}
