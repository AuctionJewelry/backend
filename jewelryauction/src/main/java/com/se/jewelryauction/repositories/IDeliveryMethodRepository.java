package com.se.jewelryauction.repositories;

import com.se.jewelryauction.models.DeliveryMethodEntity;
import com.se.jewelryauction.models.UserEntity;
import com.se.jewelryauction.models.ValuatingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IDeliveryMethodRepository extends JpaRepository<DeliveryMethodEntity, Long> {
    List<DeliveryMethodEntity> findByJewelryId(Long jewelryId);
    List<DeliveryMethodEntity> findByStaffId(Long staffId);
    List<DeliveryMethodEntity> findByJewelrySellerId(UserEntity user);
}
