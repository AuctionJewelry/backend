package com.se.jewelryauction.mappers;

import com.se.jewelryauction.models.UserEntity;
import com.se.jewelryauction.requests.CreateAccountRequest;
import com.se.jewelryauction.requests.PersonalUpdateRequest;
import com.se.jewelryauction.requests.UpdateUserRequest;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    UserEntity toModel(CreateAccountRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePersonalFromRequest(PersonalUpdateRequest updateRequest, @MappingTarget UserEntity user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "role_id", target = "role_id.id")
    void updateUserFromRequest(UpdateUserRequest updateRequest, @MappingTarget UserEntity user);
}

