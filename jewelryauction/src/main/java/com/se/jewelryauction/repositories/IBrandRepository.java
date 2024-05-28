package com.se.jewelryauction.repositories;

import com.se.jewelryauction.models.BrandEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IBrandRepository extends JpaRepository<BrandEntity, Long> {
    boolean existsByName(String name);
    BrandEntity findByName (String name);
}
