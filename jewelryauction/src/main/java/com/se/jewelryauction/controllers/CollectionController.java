package com.se.jewelryauction.controllers;

import com.se.jewelryauction.components.apis.CoreApiResponse;
import com.se.jewelryauction.models.CollectionEntity;
import com.se.jewelryauction.requests.CollectionRequest;
import com.se.jewelryauction.services.ICollectionServices;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.se.jewelryauction.mappers.CollectionMapper.INSTANCE;

@RestController
@RequestMapping("${app.api.version.v1}/collection")
@RequiredArgsConstructor
public class CollectionController {
    private final ICollectionServices collectionServices;

    @PostMapping("")
    public CoreApiResponse<CollectionEntity> createCollection(
            @Valid @RequestBody CollectionRequest request
    ){
        CollectionEntity collection = collectionServices.createCollection(INSTANCE.toModel(request));
        return CoreApiResponse.success(collection,"Insert collection successfully!");
    }

    @PutMapping("/{id}")
    public CoreApiResponse<CollectionEntity> updateCollection(
            @PathVariable Long id,
            @Valid @RequestBody CollectionRequest request
    ){
        CollectionEntity collection = collectionServices.updateCollection(id, INSTANCE.toModel(request));
        return CoreApiResponse.success(collection,"Update collection successfully!");
    }

    @GetMapping("")
    public List<CollectionEntity> getCollections(){
        List<CollectionEntity> collections = collectionServices.getAllCollections();
        return collectionServices.getAllCollections();
    }

    @GetMapping("/brand/{id}")
    public List<CollectionEntity> getCollectionsByBrand(
            @PathVariable Long id
    ){
        List<CollectionEntity> collections = collectionServices.getCollectionsByBrand(id);
        return collectionServices.getCollectionsByBrand(id);
    }

    @GetMapping("/{id}")
    public CollectionEntity getCollectionsById(
            @PathVariable Long id
    ){
        CollectionEntity collections = collectionServices.getCollectionById(id);
        return collectionServices.getCollectionById(id);
    }

    @DeleteMapping("/{id}")
    public CoreApiResponse<?> deleteCollection(
            @PathVariable Long id
    ){
        collectionServices.deleteCollection(id);
        return CoreApiResponse.success("Delete collection successfully!");
    }
}
