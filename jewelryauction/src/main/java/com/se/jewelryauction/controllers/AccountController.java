package com.se.jewelryauction.controllers;

import com.se.jewelryauction.components.apis.CoreApiResponse;
import com.se.jewelryauction.models.AuctionEntity;
import com.se.jewelryauction.models.UserEntity;
import com.se.jewelryauction.requests.AuctionRequest;
import com.se.jewelryauction.services.IAccountService;
import com.se.jewelryauction.services.IAuctionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.se.jewelryauction.mappers.AuctionMapper.INSTANCE;

@RestController
@RequestMapping("${app.api.version.v1}/account")
@RequiredArgsConstructor
public class AccountController {
    private final IAccountService accountService;

//    @PostMapping("")
//    public CoreApiResponse<UserEntity> createAuction(
//            @Valid @RequestBody AuctionRequest brandRequest
//    ){
//        AuctionEntity auction = auctionService.createAuction(INSTANCE.toModel(brandRequest));
//        return CoreApiResponse.success(auction,"Insert auction successfully");
//    }

//    @PostMapping("")
//    public CoreApiResponse<AuctionEntity> createAuction(
//            @Valid @RequestBody AuctionRequest brandRequest
//    ){
//        AuctionEntity auction = auctionService.createAuction(INSTANCE.toModel(brandRequest));
//        return CoreApiResponse.success(auction,"Insert auction successfully");
//    }

}
