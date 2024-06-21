package com.se.jewelryauction.controllers;

import com.se.jewelryauction.components.apis.CoreApiResponse;
import com.se.jewelryauction.models.AuctionEntity;
import com.se.jewelryauction.models.UserEntity;
import com.se.jewelryauction.requests.CreateAccountRequest;
import com.se.jewelryauction.services.IAccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/users/staff")
    public CoreApiResponse<List<UserEntity>> getUsersByRoleId() {
        List<UserEntity> users = accountService.getStaff();
        return CoreApiResponse.success(users);
    }
}
