package com.se.jewelryauction.repositories;

import com.se.jewelryauction.models.JewelryEntity;
import com.se.jewelryauction.models.JewelryImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IJewelryImageRepository extends JpaRepository<JewelryImageEntity, Long> {
    List<JewelryImageEntity> findByJewelryId(Long birdId);
}
