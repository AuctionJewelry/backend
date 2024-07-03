package com.se.jewelryauction.repositories;

import com.se.jewelryauction.models.SystemTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISystemTransactionRepository extends JpaRepository<SystemTransactionEntity, Long> {
}
