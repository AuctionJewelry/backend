package com.se.jewelryauction.repositories;

import com.se.jewelryauction.models.BlogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IBlogRepository extends JpaRepository<BlogEntity, Long> {
}
