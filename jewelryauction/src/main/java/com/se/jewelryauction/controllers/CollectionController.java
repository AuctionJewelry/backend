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
    public CoreApiResponse<List<CollectionEntity>> getCollections(){
        List<CollectionEntity> collections = collectionServices.getAllCollections();
        return CoreApiResponse.success(collections, "Get all collections successfully!");
    }

    @GetMapping("/brand/{id}")
    public CoreApiResponse<List<CollectionEntity>> getCollectionsByBrand(
            @PathVariable Long id
    ){
        List<CollectionEntity> collections = collectionServices.getCollectionsByBrand(id);
        return CoreApiResponse.success(collections, "Get all collections successfully!");
    }

    @GetMapping("/{id}")
    public CoreApiResponse<CollectionEntity> getCollectionsById(
            @PathVariable Long id
    ){
        CollectionEntity collections = collectionServices.getCollectionById(id);
        return CoreApiResponse.success(collections, "Get all collections successfully!");
    }

    @DeleteMapping("/{id}")
    public CoreApiResponse<?> deleteCollection(
            @PathVariable Long id
    ){
        collectionServices.deleteCollection(id);
        return CoreApiResponse.success("Delete collection successfully!");
    }
}
