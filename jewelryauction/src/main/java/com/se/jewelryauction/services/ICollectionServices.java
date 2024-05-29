package com.se.jewelryauction.services;

import com.se.jewelryauction.models.CollectionEntity;

import java.util.List;

public interface ICollectionServices {
    CollectionEntity createCollection(CollectionEntity collection) ;
    CollectionEntity   getCollectionById(long id);
    List<CollectionEntity> getAllCollections();
    List<CollectionEntity> getCollectionsByBrand(Long id);
    CollectionEntity updateCollection(long collectionId, CollectionEntity collection);
    void deleteCollection(long id);
}
