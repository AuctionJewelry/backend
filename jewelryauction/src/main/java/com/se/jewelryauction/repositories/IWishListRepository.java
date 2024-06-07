package com.se.jewelryauction.repositories;

import com.se.jewelryauction.models.WishListEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IWishListRepository extends JpaRepository <WishListEntity, Long> {
    @Query("SELECT w FROM WishListEntity w WHERE w.userId.id = :userId")
    List<WishListEntity> findByUserId(Long userId);
}

