package com.se.jewelryauction.mappers;

import com.se.jewelryauction.models.MaterialEntity;
import com.se.jewelryauction.requests.MaterialRequest;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-08T14:24:08+0700",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 18.0.2.1 (Oracle Corporation)"
)
public class MaterialMapperImpl implements MaterialMapper {

    @Override
    public MaterialEntity toModel(MaterialRequest request) {
        if ( request == null ) {
            return null;
        }

        MaterialEntity.MaterialEntityBuilder materialEntity = MaterialEntity.builder();

        materialEntity.name( request.getName() );

        return materialEntity.build();
    }
}
