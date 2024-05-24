package com.se.jewelryauction.repositories;

import com.se.jewelryauction.models.ValuatingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IValuatingRepository extends JpaRepository<ValuatingEntity, Long> {
}
