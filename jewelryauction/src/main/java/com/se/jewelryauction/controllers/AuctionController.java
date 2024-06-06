package com.se.jewelryauction.controllers;

import com.se.jewelryauction.components.apis.CoreApiResponse;
import com.se.jewelryauction.models.AuctionEntity;
import com.se.jewelryauction.models.enums.AuctionStatus;
import com.se.jewelryauction.models.enums.JewelryCondition;
import com.se.jewelryauction.models.enums.Sex;
import com.se.jewelryauction.requests.AuctionRequest;
import com.se.jewelryauction.services.IAuctionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.se.jewelryauction.mappers.AuctionMapper.INSTANCE;

@RestController
@RequestMapping("${app.api.version.v1}/auction")
@RequiredArgsConstructor
public class AuctionController {
    private final IAuctionService auctionService;

    @PostMapping("")
    public CoreApiResponse<AuctionEntity> createAuction(
            @Valid @RequestBody AuctionRequest auctionRequest
    ){
        AuctionEntity auction = auctionService.createAuction(INSTANCE.toModel(auctionRequest));
        return CoreApiResponse.success(auction,"Insert auction successfully");
    }

    @GetMapping("")
    public CoreApiResponse<List<AuctionEntity>> getAllAuctions(){
        List<AuctionEntity> auctions = auctionService.getAllAuctions();
        return CoreApiResponse.success(auctions);
    }

    @GetMapping("/{id}")
    public CoreApiResponse<AuctionEntity> getAuctionById(@Valid @PathVariable Long id){
        AuctionEntity auction = auctionService.getAuctionById(id);
        return CoreApiResponse.success(auction);
    }

    @PutMapping("/{id}")
    public CoreApiResponse<AuctionEntity> updateMaterial(
            @PathVariable Long id,
            @Valid @RequestBody AuctionStatus status
    ){
        AuctionEntity updateStatusAuction = auctionService.updateStatusAuction(id, status);
        return CoreApiResponse.success(updateStatusAuction, "Update status successfully");
    }

    @GetMapping("/category/{categoryId}")
    public List<AuctionEntity> getAuctionsByCategoryId(@PathVariable Long categoryId) {
        return auctionService.getAuctionsByCategoryId(categoryId);
    }

    @GetMapping("/category/{collectionId}")
    public List<AuctionEntity> getAuctionsByCollectionId(@PathVariable Long collectionId) {
        return auctionService.getAuctionsByCollectionId(collectionId);
    }

    @GetMapping("/myauction")
    public List<AuctionEntity> getAuctionsBySellerIdAndStatus(
            @RequestParam(required = false) AuctionStatus status) {
        return auctionService.getMyAuctionsByStatus(status);
    }

    @GetMapping("/search")
    public List<AuctionEntity> searchAuctions(
            @RequestParam(required = false) Long collectionId,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Float minPrice,
            @RequestParam(required = false) Float maxPrice,
            @RequestParam(required = false) Long brandId,
            @RequestParam(required = false) JewelryCondition jewelryCondition,
            @RequestParam(required = false) AuctionStatus status,
            @RequestParam(required = false) Sex sex) {
        return auctionService.searchAuctions(collectionId, categoryId, minPrice, maxPrice, brandId, jewelryCondition, status, sex);
    }



}
