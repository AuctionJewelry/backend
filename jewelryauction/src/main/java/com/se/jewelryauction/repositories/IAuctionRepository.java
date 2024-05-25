package com.se.jewelryauction.repositories;

import com.se.jewelryauction.models.AuctionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAuctionRepository extends JpaRepository<AuctionEntity, Long> {
}
