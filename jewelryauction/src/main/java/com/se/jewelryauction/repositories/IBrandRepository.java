package com.se.jewelryauction.repositories;

import com.se.jewelryauction.models.BrandEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IBrandRepository extends JpaRepository<BrandEntity, Long> {
    boolean existsByName(String name);
    @Query("SELECT b FROM BrandEntity b WHERE LOWER(b.name) = LOWER(:name)")
    BrandEntity findByName(@Param("name") String name);}
