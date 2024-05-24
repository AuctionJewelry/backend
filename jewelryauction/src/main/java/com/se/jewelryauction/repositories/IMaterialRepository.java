package com.se.jewelryauction.repositories;

import com.se.jewelryauction.models.MaterialEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IMaterialRepository extends JpaRepository<MaterialEntity, Long> {
    boolean existsByName(String name);
    MaterialEntity findByName (String name);
}
