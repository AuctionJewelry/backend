package com.se.jewelryauction.repositories;

import com.se.jewelryauction.models.SystemTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISystemTransactionRepository extends JpaRepository<SystemTransactionEntity, Long> {
    @Query("SELECT st FROM SystemTransactionEntity st WHERE st.sender.id = :userId OR st.receiver.id = :userId")
    List<SystemTransactionEntity> findByUserId(@Param("userId") Long userId);
}
