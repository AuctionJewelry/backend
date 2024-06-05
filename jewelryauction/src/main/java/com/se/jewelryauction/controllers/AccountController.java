package com.se.jewelryauction.controllers;

import com.se.jewelryauction.components.apis.CoreApiResponse;
import com.se.jewelryauction.models.AuctionEntity;
import com.se.jewelryauction.models.UserEntity;
import com.se.jewelryauction.requests.AuctionRequest;
import com.se.jewelryauction.requests.CreateAccountRequest;
import com.se.jewelryauction.services.IAccountService;
import com.se.jewelryauction.services.IAuctionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.se.jewelryauction.mappers.UserMapper.INSTANCE;

@RestController
@RequestMapping("${app.api.version.v1}/account")
@RequiredArgsConstructor
public class AccountController {
    private final IAccountService accountService;

//    @PostMapping("")
//    public CoreApiResponse<?> createAccountManager(
//            @Valid @RequestBody CreateAccountRequest accountRequest
//    ){
//        UserEntity user = accountService.createAccountManager(INSTANCE.toModel(accountRequest));
//        return CoreApiResponse.success("Insert auction successfully");
//    }
//
//    @PostMapping("")
//    public CoreApiResponse<AuctionEntity> createAccountStaff(
//            @Valid @RequestBody CreateAccountRequest accountRequest
//    ){
//        UserEntity user = accountService.createAccountStaff(INSTANCE.toModel(accountRequest));
//        return CoreApiResponse.success("Insert auction successfully");
//    }

}
