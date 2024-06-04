package com.se.jewelryauction.controllers;

import com.se.jewelryauction.components.apis.CoreApiResponse;
import com.se.jewelryauction.models.AuctionEntity;
import com.se.jewelryauction.models.BrandEntity;
import com.se.jewelryauction.models.enums.AuctionStatus;
import com.se.jewelryauction.requests.AuctionRequest;
import com.se.jewelryauction.requests.BrandRequest;
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
            @Valid @RequestBody AuctionRequest brandRequest
    ){
        AuctionEntity auction = auctionService.createAuction(INSTANCE.toModel(brandRequest));
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


}
