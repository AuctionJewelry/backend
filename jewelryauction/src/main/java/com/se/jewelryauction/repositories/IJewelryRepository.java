package com.se.jewelryauction.repositories;

import com.se.jewelryauction.models.JewelryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IJewelryRepository extends JpaRepository<JewelryEntity, Long> {
    boolean existsByName(String name);
    JewelryEntity findByName (String name);
}
