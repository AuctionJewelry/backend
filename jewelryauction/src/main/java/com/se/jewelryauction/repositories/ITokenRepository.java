package com.se.jewelryauction.repositories;

import com.se.jewelryauction.models.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ITokenRepository extends JpaRepository<TokenEntity, Long> {
    Optional<TokenEntity> findByName(String name);

}
