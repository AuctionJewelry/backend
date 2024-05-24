package com.se.jewelryauction.repositories;

import com.se.jewelryauction.models.CollectionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICollectionRepository extends JpaRepository<CollectionEntity, Long> {
}
