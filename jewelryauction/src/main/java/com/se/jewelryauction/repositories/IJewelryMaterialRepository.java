package com.se.jewelryauction.repositories;

import com.se.jewelryauction.models.JewelryEntity;
import com.se.jewelryauction.models.JewelryMaterialEntity;
import com.se.jewelryauction.models.MaterialEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IJewelryMaterialRepository extends JpaRepository<JewelryMaterialEntity, Long> {
    List<JewelryMaterialEntity> findByJewelryId(Long id);

    @Query("SELECT j FROM JewelryMaterialEntity j WHERE j.jewelry = :jewelry AND j.material = :material")
    JewelryMaterialEntity findByJewelryAndMaterial(@Param("jewelry") JewelryEntity jewelry, @Param("material") MaterialEntity material);
}
