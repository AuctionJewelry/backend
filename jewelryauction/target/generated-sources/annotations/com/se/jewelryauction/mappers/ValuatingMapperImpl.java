package com.se.jewelryauction.mappers;

import com.se.jewelryauction.models.JewelryEntity;
import com.se.jewelryauction.models.UserEntity;
import com.se.jewelryauction.models.ValuatingEntity;
import com.se.jewelryauction.requests.UpdateValuatingRequest;
import com.se.jewelryauction.requests.ValuatingRequest;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-08T14:24:08+0700",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 18.0.2.1 (Oracle Corporation)"
)
public class ValuatingMapperImpl implements ValuatingMapper {

    @Override
    public ValuatingEntity toModel(ValuatingRequest request) {
        if ( request == null ) {
            return null;
        }

        ValuatingEntity.ValuatingEntityBuilder valuatingEntity = ValuatingEntity.builder();

        valuatingEntity.staff( valuatingRequestToUserEntity( request ) );
        valuatingEntity.jewelry( valuatingRequestToJewelryEntity( request ) );
        valuatingEntity.isOnline( request.isOnline() );
        valuatingEntity.notes( request.getNotes() );
        valuatingEntity.desiredPrice( request.getDesiredPrice() );
        valuatingEntity.paymentMethod( request.getPaymentMethod() );
        valuatingEntity.valuatingMethod( request.getValuatingMethod() );

        return valuatingEntity.build();
    }

    @Override
    public ValuatingEntity toModel(UpdateValuatingRequest request) {
        if ( request == null ) {
            return null;
        }

        ValuatingEntity.ValuatingEntityBuilder valuatingEntity = ValuatingEntity.builder();

        valuatingEntity.staff( updateValuatingRequestToUserEntity( request ) );
        valuatingEntity.valuation_value( request.getValuation_value() );
        valuatingEntity.notes( request.getNotes() );
        valuatingEntity.status( request.getStatus() );
        valuatingEntity.desiredPrice( request.getDesiredPrice() );
        valuatingEntity.paymentMethod( request.getPaymentMethod() );
        valuatingEntity.valuatingMethod( request.getValuatingMethod() );
        valuatingEntity.address( request.getAddress() );

        return valuatingEntity.build();
    }

    protected UserEntity valuatingRequestToUserEntity(ValuatingRequest valuatingRequest) {
        if ( valuatingRequest == null ) {
            return null;
        }

        UserEntity.UserEntityBuilder userEntity = UserEntity.builder();

        userEntity.id( (long) valuatingRequest.getStaffId() );

        return userEntity.build();
    }

    protected JewelryEntity valuatingRequestToJewelryEntity(ValuatingRequest valuatingRequest) {
        if ( valuatingRequest == null ) {
            return null;
        }

        JewelryEntity.JewelryEntityBuilder jewelryEntity = JewelryEntity.builder();

        jewelryEntity.id( (long) valuatingRequest.getJewelryId() );

        return jewelryEntity.build();
    }

    protected UserEntity updateValuatingRequestToUserEntity(UpdateValuatingRequest updateValuatingRequest) {
        if ( updateValuatingRequest == null ) {
            return null;
        }

        UserEntity.UserEntityBuilder userEntity = UserEntity.builder();

        userEntity.id( (long) updateValuatingRequest.getStaffId() );

        return userEntity.build();
    }
}
