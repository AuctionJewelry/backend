package com.se.jewelryauction.repositories;

import com.se.jewelryauction.models.UserEntity;
import com.se.jewelryauction.models.WalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IWalletRepository extends JpaRepository<WalletEntity, Long> {
    WalletEntity findByUser(UserEntity user);
}
