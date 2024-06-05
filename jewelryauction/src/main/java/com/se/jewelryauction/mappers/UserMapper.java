package com.se.jewelryauction.mappers;

import com.se.jewelryauction.models.UserEntity;
import com.se.jewelryauction.requests.CreateAccountRequest;
import com.se.jewelryauction.requests.PersonalUpdateRequest;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    @Mapping(source = "role_id", target = "role_id.id")
    UserEntity toModel(CreateAccountRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public void updatePersonalFromRequest(PersonalUpdateRequest updateRequest, @MappingTarget UserEntity user);
}
