package com.se.jewelryauction.mappers;

import com.se.jewelryauction.models.JewelryImageEntity;
import com.se.jewelryauction.responses.JewelryImageResponse;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-08T14:24:08+0700",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 18.0.2.1 (Oracle Corporation)"
)
public class JewelryImageMapperImpl implements JewelryImageMapper {

    @Override
    public JewelryImageResponse toResponse(JewelryImageEntity jewelryImage) {
        if ( jewelryImage == null ) {
            return null;
        }

        JewelryImageResponse.JewelryImageResponseBuilder jewelryImageResponse = JewelryImageResponse.builder();

        if ( jewelryImage.getId() != null ) {
            jewelryImageResponse.id( jewelryImage.getId().intValue() );
        }

        return jewelryImageResponse.build();
    }

    @Override
    public List<JewelryImageResponse> toListResponse(List<JewelryImageEntity> image) {
        if ( image == null ) {
            return null;
        }

        List<JewelryImageResponse> list = new ArrayList<JewelryImageResponse>( image.size() );
        for ( JewelryImageEntity jewelryImageEntity : image ) {
            list.add( toResponse( jewelryImageEntity ) );
        }

        return list;
    }
}
