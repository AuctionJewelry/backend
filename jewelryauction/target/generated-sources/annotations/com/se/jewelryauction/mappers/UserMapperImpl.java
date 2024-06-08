package com.se.jewelryauction.mappers;

import com.se.jewelryauction.models.RoleEntity;
import com.se.jewelryauction.models.UserEntity;
import com.se.jewelryauction.requests.CreateAccountRequest;
import com.se.jewelryauction.requests.PersonalUpdateRequest;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-08T14:24:08+0700",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 18.0.2.1 (Oracle Corporation)"
)
public class UserMapperImpl implements UserMapper {

    @Override
    public UserEntity toModel(CreateAccountRequest request) {
        if ( request == null ) {
            return null;
        }

        UserEntity.UserEntityBuilder userEntity = UserEntity.builder();

        userEntity.role_id( createAccountRequestToRoleEntity( request ) );
        userEntity.full_name( request.getFull_name() );
        userEntity.phone_number( request.getPhone_number() );
        userEntity.email( request.getEmail() );
        userEntity.password( request.getPassword() );
        userEntity.address( request.getAddress() );
        userEntity.date_of_birth( request.getDate_of_birth() );

        return userEntity.build();
    }

    @Override
    public void updatePersonalFromRequest(PersonalUpdateRequest updateRequest, UserEntity user) {
        if ( updateRequest == null ) {
            return;
        }

        if ( updateRequest.getPassword() != null ) {
            user.setPassword( updateRequest.getPassword() );
        }
        if ( updateRequest.getAddress() != null ) {
            user.setAddress( updateRequest.getAddress() );
        }
    }

    protected RoleEntity createAccountRequestToRoleEntity(CreateAccountRequest createAccountRequest) {
        if ( createAccountRequest == null ) {
            return null;
        }

        RoleEntity.RoleEntityBuilder roleEntity = RoleEntity.builder();

        if ( createAccountRequest.getRole_id() != null ) {
            roleEntity.id( Long.parseLong( createAccountRequest.getRole_id() ) );
        }

        return roleEntity.build();
    }
}
