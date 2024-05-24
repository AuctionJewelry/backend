package com.se.jewelryauction.repositories;

import com.se.jewelryauction.models.JewelryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IJewelryImageRepository extends JpaRepository<JewelryEntity, Long> {
}
