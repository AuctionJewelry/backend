package com.se.jewelryauction.services.implement;

import com.se.jewelryauction.components.exceptions.AppException;
import com.se.jewelryauction.models.BrandEntity;
import com.se.jewelryauction.models.CollectionEntity;
import com.se.jewelryauction.repositories.IBrandRepository;
import com.se.jewelryauction.repositories.ICollectionRepository;
import com.se.jewelryauction.services.ICollectionServices;
import com.se.jewelryauction.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CollectionService implements ICollectionServices {
    private final ICollectionRepository collectionRepository;
    private final IBrandRepository brandRepository;

    @Override
    public CollectionEntity createCollection(CollectionEntity collection) {
        collection.setName(StringUtils.NameStandardlizing(collection.getName()));
        CollectionEntity collectionEntity = collectionRepository
                .findByName(collection.getName());
        BrandEntity brand = null;
        if(collectionEntity == null){
            String name = StringUtils.NameStandardlizing(collection.getBrand().getName());
            brand = brandRepository.findByName(name);
            if (brand == null){
                brand = brandRepository.save(collection.getBrand());
            }
        }
        else{
            String name = StringUtils.NameStandardlizing(collection.getBrand().getName());
            brand = brandRepository.findByName(name);
            if(brand != null){
                if(brand.getName().equals(collection.getBrand().getName())){
                    throw new AppException(HttpStatus.BAD_REQUEST, "Duplicated collection!");
                }
            }
            else{
                brand = brandRepository.save(collection.getBrand());
            }
        }
        collection.setBrand(brand);
        return collectionRepository.save(collection);
    }

    @Override
    public CollectionEntity getCollectionById(long id) {
        return collectionRepository.findById(id)
                .orElseThrow(() ->
                        new AppException(HttpStatus.BAD_REQUEST, "There are no collection"));
    }

    @Override
    public List<CollectionEntity> getAllCollections() {
        return collectionRepository.findAll();
    }

    @Override
    public List<CollectionEntity> getCollectionsByBrand(Long id) {
        List<CollectionEntity> results = new ArrayList<>();
        List<CollectionEntity> collections = this.getAllCollections();
        for(int i = 0; i < collections.size(); i++){
            if(collections.get(i).getBrand().getId() == id){
                results.add(collections.get(i));
            }
        }
        return results;
    }

    @Override
    public CollectionEntity updateCollection(long collectionId, CollectionEntity collection) {
        CollectionEntity existingCollectionByID = this.getCollectionById(collectionId);
        collection.setName(StringUtils.NameStandardlizing(collection.getName()));
        CollectionEntity collectionEntity = collectionRepository
                .findByName(collection.getName());
        BrandEntity brand;
        if(collectionEntity == null){
            String brandName = StringUtils.NameStandardlizing(collection.getBrand().getName());
            brand = brandRepository.findByName(brandName);
            if (brand == null){
                brand = brandRepository.save(collection.getBrand());
            }
        }
        else{
            String brandName = StringUtils.NameStandardlizing(collection.getBrand().getName());
            brand = brandRepository.findByName(brandName);
            if(brand != null){
                if(brand.getName().equals(collection.getBrand().getName())){
                    throw new AppException(HttpStatus.BAD_REQUEST, "Duplicated collection!");
                }
            }
            else{
                brand = brandRepository.save(collection.getBrand());
            }

        }
        existingCollectionByID.setName(collection.getName());
        existingCollectionByID.setBrand(brand);

        return collectionRepository.save(existingCollectionByID);
    }

    @Override
    public void deleteCollection(long id) {
        CollectionEntity deletedCollection = this.getCollectionById(id);
        if(deletedCollection != null){
            collectionRepository.delete(deletedCollection);
        }
    }
}
