package com.se.jewelryauction.repositories;

import com.se.jewelryauction.models.BlogImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IBlogImageRepository extends JpaRepository<BlogImageEntity, Long> {
}
