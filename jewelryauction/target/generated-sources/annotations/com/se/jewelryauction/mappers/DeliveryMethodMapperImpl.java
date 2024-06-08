package com.se.jewelryauction.mappers;

import com.se.jewelryauction.models.DeliveryMethodEntity;
import com.se.jewelryauction.models.JewelryEntity;
import com.se.jewelryauction.models.UserEntity;
import com.se.jewelryauction.requests.DeliveryMethodRequest;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-08T14:24:08+0700",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 18.0.2.1 (Oracle Corporation)"
)
public class DeliveryMethodMapperImpl implements DeliveryMethodMapper {

    @Override
    public DeliveryMethodEntity toModel(DeliveryMethodRequest deliveryMethod) {
        if ( deliveryMethod == null ) {
            return null;
        }

        DeliveryMethodEntity.DeliveryMethodEntityBuilder deliveryMethodEntity = DeliveryMethodEntity.builder();

        deliveryMethodEntity.user( deliveryMethodRequestToUserEntity( deliveryMethod ) );
        deliveryMethodEntity.jewelry( deliveryMethodRequestToJewelryEntity( deliveryMethod ) );
        deliveryMethodEntity.staff( deliveryMethodRequestToUserEntity1( deliveryMethod ) );
        deliveryMethodEntity.phone_number( deliveryMethod.getPhone_number() );
        deliveryMethodEntity.full_name( deliveryMethod.getFull_name() );
        deliveryMethodEntity.address( deliveryMethod.getAddress() );
        deliveryMethodEntity.status( deliveryMethod.getStatus() );

        return deliveryMethodEntity.build();
    }

    protected UserEntity deliveryMethodRequestToUserEntity(DeliveryMethodRequest deliveryMethodRequest) {
        if ( deliveryMethodRequest == null ) {
            return null;
        }

        UserEntity.UserEntityBuilder userEntity = UserEntity.builder();

        userEntity.id( (long) deliveryMethodRequest.getRequestUserId() );

        return userEntity.build();
    }

    protected JewelryEntity deliveryMethodRequestToJewelryEntity(DeliveryMethodRequest deliveryMethodRequest) {
        if ( deliveryMethodRequest == null ) {
            return null;
        }

        JewelryEntity.JewelryEntityBuilder jewelryEntity = JewelryEntity.builder();

        jewelryEntity.id( (long) deliveryMethodRequest.getJewelryId() );

        return jewelryEntity.build();
    }

    protected UserEntity deliveryMethodRequestToUserEntity1(DeliveryMethodRequest deliveryMethodRequest) {
        if ( deliveryMethodRequest == null ) {
            return null;
        }

        UserEntity.UserEntityBuilder userEntity = UserEntity.builder();

        userEntity.id( (long) deliveryMethodRequest.getStaffId() );

        return userEntity.build();
    }
}
