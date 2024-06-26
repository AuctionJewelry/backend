package com.se.jewelryauction.repositories;

import com.se.jewelryauction.models.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICategoryRepository extends JpaRepository<CategoryEntity, Long> {
    boolean existsByName(String name);
}
