package com.se.jewelryauction.repositories;

import com.se.jewelryauction.models.JewelryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IJewelryRepository extends JpaRepository<JewelryEntity, Long> {
    boolean existsByName(String name);
    JewelryEntity findByName (String name);
    @Query("SELECT j FROM JewelryEntity j WHERE j.sellerId.id = :sellerId")
    List<JewelryEntity> findBySellerId(Long sellerId);

}
