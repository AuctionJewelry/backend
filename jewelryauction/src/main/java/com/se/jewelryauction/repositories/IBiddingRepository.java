package com.se.jewelryauction.repositories;

import com.se.jewelryauction.models.BiddingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IBiddingRepository extends JpaRepository<BiddingEntity, Long> {
}
