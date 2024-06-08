package com.se.jewelryauction.mappers;

import com.se.jewelryauction.models.AuctionEntity;
import com.se.jewelryauction.models.JewelryEntity;
import com.se.jewelryauction.requests.AuctionRequest;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-08T14:24:08+0700",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 18.0.2.1 (Oracle Corporation)"
)
public class AuctionMapperImpl implements AuctionMapper {

    @Override
    public AuctionEntity toModel(AuctionRequest request) {
        if ( request == null ) {
            return null;
        }

        AuctionEntity.AuctionEntityBuilder auctionEntity = AuctionEntity.builder();

        auctionEntity.jewelry( auctionRequestToJewelryEntity( request ) );
        auctionEntity.startTime( request.getStartTime() );
        auctionEntity.endTime( request.getEndTime() );

        return auctionEntity.build();
    }

    protected JewelryEntity auctionRequestToJewelryEntity(AuctionRequest auctionRequest) {
        if ( auctionRequest == null ) {
            return null;
        }

        JewelryEntity.JewelryEntityBuilder jewelryEntity = JewelryEntity.builder();

        jewelryEntity.id( auctionRequest.getJewelryId() );

        return jewelryEntity.build();
    }
}
