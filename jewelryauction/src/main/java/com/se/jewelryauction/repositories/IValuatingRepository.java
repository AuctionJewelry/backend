package com.se.jewelryauction.repositories;

import com.se.jewelryauction.models.UserEntity;
import com.se.jewelryauction.models.ValuatingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IValuatingRepository extends JpaRepository<ValuatingEntity, Long> {
    List<ValuatingEntity> findByJewelryId(Long jewelryId);
    List<ValuatingEntity> findByStaffId(Long staffId);
    List<ValuatingEntity> findByJewelrySellerId(UserEntity user);

}
