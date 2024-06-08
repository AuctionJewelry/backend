package com.se.jewelryauction.mappers;

import com.se.jewelryauction.models.BrandEntity;
import com.se.jewelryauction.requests.BrandRequest;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-08T14:24:07+0700",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 18.0.2.1 (Oracle Corporation)"
)
public class BrandMapperImpl implements BrandMapper {

    @Override
    public BrandEntity toModel(BrandRequest request) {
        if ( request == null ) {
            return null;
        }

        BrandEntity.BrandEntityBuilder brandEntity = BrandEntity.builder();

        brandEntity.name( request.getName() );

        return brandEntity.build();
    }
}
