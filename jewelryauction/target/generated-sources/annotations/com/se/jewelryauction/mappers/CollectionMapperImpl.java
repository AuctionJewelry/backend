package com.se.jewelryauction.mappers;

import com.se.jewelryauction.models.BrandEntity;
import com.se.jewelryauction.models.CollectionEntity;
import com.se.jewelryauction.requests.CollectionRequest;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-08T14:24:08+0700",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 18.0.2.1 (Oracle Corporation)"
)
public class CollectionMapperImpl implements CollectionMapper {

    @Override
    public CollectionEntity toModel(CollectionRequest collectionRequest) {
        if ( collectionRequest == null ) {
            return null;
        }

        CollectionEntity.CollectionEntityBuilder collectionEntity = CollectionEntity.builder();

        collectionEntity.brand( collectionRequestToBrandEntity( collectionRequest ) );
        collectionEntity.name( collectionRequest.getName() );

        return collectionEntity.build();
    }

    protected BrandEntity collectionRequestToBrandEntity(CollectionRequest collectionRequest) {
        if ( collectionRequest == null ) {
            return null;
        }

        BrandEntity.BrandEntityBuilder brandEntity = BrandEntity.builder();

        brandEntity.name( collectionRequest.getBrand() );

        return brandEntity.build();
    }
}
