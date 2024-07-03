package com.se.jewelryauction.controllers;

import com.se.jewelryauction.components.apis.CoreApiResponse;
import com.se.jewelryauction.models.RoleEntity;
import com.se.jewelryauction.models.UserEntity;
import com.se.jewelryauction.requests.CreateAccountRequest;
import com.se.jewelryauction.requests.UpdateUserRequest;
import com.se.jewelryauction.services.IAccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.se.jewelryauction.mappers.UserMapper.INSTANCE;

@RestController
@RequestMapping("${app.api.version.v1}/account")
@RequiredArgsConstructor
public class AccountController {
    private final IAccountService accountService;

    @PostMapping("/users/manager")
    public CoreApiResponse<?> createAccountManager(
            @Valid @RequestBody CreateAccountRequest accountRequest
    ){
        UserEntity user = accountService.createAccountManager(INSTANCE.toModel(accountRequest));
        return CoreApiResponse.success("Insert manager successfully");
    }

    @PostMapping("/users/staff")
    public CoreApiResponse<?> createAccountStaff(
            @Valid @RequestBody CreateAccountRequest accountRequest
    ){
        UserEntity user = accountService.createAccountStaff(INSTANCE.toModel(accountRequest));
        return CoreApiResponse.success("Insert staff successfully");
    }

    @GetMapping("/staffs")
    public CoreApiResponse<List<UserEntity>> getStaff() {
        List<UserEntity> users = accountService.getStaff();
        return CoreApiResponse.success(users);
    }

    @GetMapping("/managers")
    public CoreApiResponse<List<UserEntity>> getManager() {
        List<UserEntity> users = accountService.getManager();
        return CoreApiResponse.success(users);
    }
    @GetMapping("/{id}")
    public CoreApiResponse<UserEntity> getUserById(@PathVariable Long id) {
        return CoreApiResponse.success(accountService.getUserById(id));
    }

    @PutMapping("/{id}")
    public CoreApiResponse<UserEntity> updateUser(@PathVariable Long id, @RequestBody UpdateUserRequest updateUserRequest) {
        return CoreApiResponse.success(accountService.updateUser(id, updateUserRequest));
    }
    @GetMapping
    public CoreApiResponse<List<UserEntity>> getAllUsers() {
        return CoreApiResponse.success(accountService.getAllUser());
    }

    @PutMapping("/{id}/ban")
    public CoreApiResponse<UserEntity> banUser(@PathVariable Long id) {
        return CoreApiResponse.success(accountService.banUser(id));
    }

    @GetMapping("/roles")
    public CoreApiResponse<List<RoleEntity>> getAllRoles() {
        return CoreApiResponse.success(accountService.getAllRoles());
    }
}
