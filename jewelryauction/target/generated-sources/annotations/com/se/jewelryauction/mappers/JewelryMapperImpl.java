package com.se.jewelryauction.mappers;

import com.se.jewelryauction.models.BrandEntity;
import com.se.jewelryauction.models.CategoryEntity;
import com.se.jewelryauction.models.CollectionEntity;
import com.se.jewelryauction.models.JewelryEntity;
import com.se.jewelryauction.models.JewelryMaterialEntity;
import com.se.jewelryauction.models.enums.JewelryCondition;
import com.se.jewelryauction.models.enums.Sex;
import com.se.jewelryauction.requests.JewelryRequest;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-08T14:24:08+0700",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 18.0.2.1 (Oracle Corporation)"
)
@Component
public class JewelryMapperImpl implements JewelryMapper {

    @Override
    public JewelryEntity toModel(JewelryRequest request) {
        if ( request == null ) {
            return null;
        }

        JewelryEntity.JewelryEntityBuilder jewelryEntity = JewelryEntity.builder();

        jewelryEntity.brand( jewelryRequestToBrandEntity( request ) );
        jewelryEntity.collection( jewelryRequestToCollectionEntity( request ) );
        jewelryEntity.category( jewelryRequestToCategoryEntity( request ) );
        jewelryEntity.jewelryMaterials( mapMaterials( request.getMaterials() ) );
        jewelryEntity.name( request.getName() );
        jewelryEntity.description( request.getDescription() );
        jewelryEntity.weight( request.getWeight() );
        jewelryEntity.size( request.getSize() );
        jewelryEntity.color( request.getColor() );
        if ( request.getSex() != null ) {
            jewelryEntity.sex( Enum.valueOf( Sex.class, request.getSex() ) );
        }
        if ( request.getJewelryCondition() != null ) {
            jewelryEntity.jewelryCondition( Enum.valueOf( JewelryCondition.class, request.getJewelryCondition() ) );
        }

        return jewelryEntity.build();
    }

    @Override
    public void updateJewelryFromRequest(JewelryRequest request, JewelryEntity bird) {
        if ( request == null ) {
            return;
        }

        if ( bird.getCategory() == null ) {
            bird.setCategory( CategoryEntity.builder().build() );
        }
        jewelryRequestToCategoryEntity1( request, bird.getCategory() );
        if ( bird.getBrand() == null ) {
            bird.setBrand( BrandEntity.builder().build() );
        }
        jewelryRequestToBrandEntity1( request, bird.getBrand() );
        if ( bird.getCollection() == null ) {
            bird.setCollection( CollectionEntity.builder().build() );
        }
        jewelryRequestToCollectionEntity1( request, bird.getCollection() );
        if ( bird.getJewelryMaterials() != null ) {
            List<JewelryMaterialEntity> list = mapMaterials( request.getMaterials() );
            if ( list != null ) {
                bird.getJewelryMaterials().clear();
                bird.getJewelryMaterials().addAll( list );
            }
        }
        else {
            List<JewelryMaterialEntity> list = mapMaterials( request.getMaterials() );
            if ( list != null ) {
                bird.setJewelryMaterials( list );
            }
        }
        if ( request.getName() != null ) {
            bird.setName( request.getName() );
        }
        if ( request.getDescription() != null ) {
            bird.setDescription( request.getDescription() );
        }
        bird.setWeight( request.getWeight() );
        if ( request.getSize() != null ) {
            bird.setSize( request.getSize() );
        }
        if ( request.getColor() != null ) {
            bird.setColor( request.getColor() );
        }
        if ( request.getSex() != null ) {
            bird.setSex( Enum.valueOf( Sex.class, request.getSex() ) );
        }
        if ( request.getJewelryCondition() != null ) {
            bird.setJewelryCondition( Enum.valueOf( JewelryCondition.class, request.getJewelryCondition() ) );
        }
    }

    protected BrandEntity jewelryRequestToBrandEntity(JewelryRequest jewelryRequest) {
        if ( jewelryRequest == null ) {
            return null;
        }

        BrandEntity.BrandEntityBuilder brandEntity = BrandEntity.builder();

        brandEntity.name( jewelryRequest.getBrand() );

        return brandEntity.build();
    }

    protected CollectionEntity jewelryRequestToCollectionEntity(JewelryRequest jewelryRequest) {
        if ( jewelryRequest == null ) {
            return null;
        }

        CollectionEntity.CollectionEntityBuilder collectionEntity = CollectionEntity.builder();

        collectionEntity.name( jewelryRequest.getCollection() );

        return collectionEntity.build();
    }

    protected CategoryEntity jewelryRequestToCategoryEntity(JewelryRequest jewelryRequest) {
        if ( jewelryRequest == null ) {
            return null;
        }

        CategoryEntity.CategoryEntityBuilder categoryEntity = CategoryEntity.builder();

        categoryEntity.id( jewelryRequest.getCategory() );

        return categoryEntity.build();
    }

    protected void jewelryRequestToCategoryEntity1(JewelryRequest jewelryRequest, CategoryEntity mappingTarget) {
        if ( jewelryRequest == null ) {
            return;
        }

        if ( jewelryRequest.getCategory() != null ) {
            mappingTarget.setId( jewelryRequest.getCategory() );
        }
    }

    protected void jewelryRequestToBrandEntity1(JewelryRequest jewelryRequest, BrandEntity mappingTarget) {
        if ( jewelryRequest == null ) {
            return;
        }

        if ( jewelryRequest.getBrand() != null ) {
            mappingTarget.setName( jewelryRequest.getBrand() );
        }
    }

    protected void jewelryRequestToCollectionEntity1(JewelryRequest jewelryRequest, CollectionEntity mappingTarget) {
        if ( jewelryRequest == null ) {
            return;
        }

        if ( jewelryRequest.getCollection() != null ) {
            mappingTarget.setName( jewelryRequest.getCollection() );
        }
    }
}
