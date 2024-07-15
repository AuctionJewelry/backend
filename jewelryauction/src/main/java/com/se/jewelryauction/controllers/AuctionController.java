package com.se.jewelryauction.controllers;

import com.se.jewelryauction.components.apis.CoreApiResponse;
import com.se.jewelryauction.models.AuctionEntity;
import com.se.jewelryauction.models.enums.AuctionStatus;
import com.se.jewelryauction.models.enums.JewelryCondition;
import com.se.jewelryauction.models.enums.Sex;
import com.se.jewelryauction.requests.AuctionRequest;
import com.se.jewelryauction.requests.UpdateTimeAuctionRequest;
import com.se.jewelryauction.responses.AuctionResponse;
import com.se.jewelryauction.responses.ListBidForAuction;
import com.se.jewelryauction.services.IAuctionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.se.jewelryauction.mappers.AuctionMapper.INSTANCE;

@RestController
@RequestMapping("${app.api.version.v1}/auction")
@RequiredArgsConstructor
public class AuctionController {
    private final IAuctionService auctionService;
    @PreAuthorize("hasRole('USER')")
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

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/time/{id}")
    public CoreApiResponse<AuctionEntity> updateTime(@PathVariable Long id, @RequestBody UpdateTimeAuctionRequest request) {
        AuctionEntity updatedAuction = auctionService.updateTime(id, request);
        return CoreApiResponse.success(updatedAuction,"Update auction successfully");
    }

    @GetMapping("/category/{categoryId}")
    public CoreApiResponse<List<AuctionEntity>> getAuctionsByCategoryId(@PathVariable Long categoryId) {
        return CoreApiResponse.success(auctionService.getAuctionsByCategoryId(categoryId));
    }

    @GetMapping("/collection/{collectionId}")
    public CoreApiResponse<List<AuctionEntity>> getAuctionsByCollectionId(@PathVariable Long collectionId) {
        return CoreApiResponse.success(auctionService.getAuctionsByCollectionId(collectionId));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/myauction")
    public CoreApiResponse<List<AuctionEntity>>getAuctionsBySellerIdAndStatus(
            @RequestParam(required = false) AuctionStatus status) {
        return CoreApiResponse.success(auctionService.getMyAuctionsByStatus(status));
    }

    @GetMapping("/viewauction")
    public CoreApiResponse<Page<AuctionEntity>> viewAuction(
            @RequestParam(required = false) Long collectionId,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Float minPrice,
            @RequestParam(required = false) Float maxPrice,
            @RequestParam(required = false) Long brandId,
            @RequestParam(required = false) JewelryCondition jewelryCondition,
            @RequestParam(required = false) Sex sex,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int limit) {
        PageRequest pageRequest = PageRequest.of(
                page, limit,
                Sort.by("createdAt").descending()
        );
        return CoreApiResponse.success(auctionService.searchAuctions(collectionId, categoryId, minPrice, maxPrice, brandId, jewelryCondition, AuctionStatus.InProgress, sex,pageRequest));
    }

    @GetMapping("/admin/search")
    public Page<AuctionEntity> searchAuctions(
            @RequestParam(required = false) Long collectionId,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Float minPrice,
            @RequestParam(required = false) Float maxPrice,
            @RequestParam(required = false) Long brandId,
            @RequestParam(required = false) JewelryCondition jewelryCondition,
            @RequestParam(required = false) AuctionStatus status,
            @RequestParam(required = false) Sex sex,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int limit) {
        PageRequest pageRequest = PageRequest.of(
                page, limit,
                Sort.by("createdAt").descending()
        );
        return auctionService.searchAuctions(collectionId, categoryId, minPrice, maxPrice, brandId, jewelryCondition, status, sex,pageRequest);
    }

    @DeleteMapping("/cancel/{id}")
    public CoreApiResponse<?> cancalAuctions(
            @PathVariable Long id
    ){
        auctionService.cancelAuction(id);
        return CoreApiResponse.success("Cancel auction successfully");
    }

    @GetMapping("/bids/{auctionId}")
    public CoreApiResponse<List<ListBidForAuction>> getBidsByAuctionId(@PathVariable Long auctionId) {
        return CoreApiResponse.success(auctionService.getBidsByAuctionId(auctionId)) ;
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/join/user")
    public CoreApiResponse<List<AuctionResponse>>getAuctionsByUserId() {
        return CoreApiResponse.success(auctionService.getAuctionsByUserId());
    }
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/win")
    public CoreApiResponse<List<AuctionEntity>> getAuctionWin() {
        return CoreApiResponse.success(auctionService.getAuctionsWin());
    }

    @GetMapping("/bidders/count/{auctionId}")
    public CoreApiResponse<?> getUniqueBidderCount(@PathVariable Long auctionId) {
        int count = auctionService.countUniqueBidders(auctionId);
        return CoreApiResponse.success("Total bidders: " + count);
    }

}
