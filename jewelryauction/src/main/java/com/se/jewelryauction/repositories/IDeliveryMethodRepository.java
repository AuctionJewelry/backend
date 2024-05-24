package com.se.jewelryauction.repositories;

import com.se.jewelryauction.models.DeliveryMethodEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IDeliveryMethodRepository extends JpaRepository<DeliveryMethodEntity, Long> {
}
