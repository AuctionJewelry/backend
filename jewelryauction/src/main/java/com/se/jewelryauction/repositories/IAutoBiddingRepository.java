package com.se.jewelryauction.repositories;

import com.se.jewelryauction.models.AutoBiddingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAutoBiddingRepository extends JpaRepository<AutoBiddingEntity, Long> {
}
