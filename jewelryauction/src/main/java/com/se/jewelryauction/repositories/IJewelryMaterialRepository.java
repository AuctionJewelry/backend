package com.se.jewelryauction.repositories;

import com.se.jewelryauction.models.JewelryEntity;
import com.se.jewelryauction.models.JewelryMaterialEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IJewelryMaterialRepository extends JpaRepository<JewelryMaterialEntity, Long> {
    List<JewelryMaterialEntity> findByJewelryId(Long id);
}
