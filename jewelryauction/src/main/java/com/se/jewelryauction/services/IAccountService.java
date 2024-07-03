package com.se.jewelryauction.services;

import com.se.jewelryauction.models.BrandEntity;
import com.se.jewelryauction.models.RoleEntity;
import com.se.jewelryauction.models.UserEntity;
import com.se.jewelryauction.requests.PersonalUpdateRequest;
import com.se.jewelryauction.requests.UpdateUserRequest;

import java.util.List;

public interface IAccountService {
    UserEntity createAccountStaff(UserEntity user);

    UserEntity createAccountShipper(UserEntity user);

    UserEntity createAccountManager(UserEntity user);

    UserEntity getUserById(Long id);
    UserEntity updateUser(Long id, UpdateUserRequest update);

    List<UserEntity> getAllUser();

    UserEntity banUser(Long id);

    List<UserEntity> getStaff();

    List<RoleEntity> getAllRoles();

    List<UserEntity> getManager();
}
