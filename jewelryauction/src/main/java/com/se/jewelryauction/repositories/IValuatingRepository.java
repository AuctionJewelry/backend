package com.se.jewelryauction.repositories;

import com.se.jewelryauction.models.JewelryEntity;
import com.se.jewelryauction.models.UserEntity;
import com.se.jewelryauction.models.ValuatingEntity;
import com.se.jewelryauction.models.enums.ValuatingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IValuatingRepository extends JpaRepository<ValuatingEntity, Long> {
    List<ValuatingEntity> findByJewelryId(Long jewelryId);
    List<ValuatingEntity> findByStaffId(Long staffId);
    List<ValuatingEntity> findByJewelrySellerId(UserEntity user);
    List<ValuatingEntity> findByStatusAndStaffId(ValuatingStatus status, Long staffId);

    @Query("SELECT v FROM ValuatingEntity v WHERE v.jewelry = :jewelry AND v.isOnline = false")
    Optional<ValuatingEntity> findByJewelryAndIsOnlineFalse(@Param("jewelry") JewelryEntity jewelry);

}
