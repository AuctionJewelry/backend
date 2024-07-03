package com.se.jewelryauction.repositories;

import com.se.jewelryauction.models.SystemWalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ISystemWalletRepository extends JpaRepository<SystemWalletEntity, Long> {
    @Query(value = "SELECT * FROM system_wallets ORDER BY updated_at DESC LIMIT 1", nativeQuery = true)
    SystemWalletEntity findLatestSystemWallet();
}
