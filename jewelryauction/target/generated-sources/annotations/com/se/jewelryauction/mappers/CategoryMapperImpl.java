package com.se.jewelryauction.mappers;

import com.se.jewelryauction.models.CategoryEntity;
import com.se.jewelryauction.requests.CategoryRequest;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-08T14:24:08+0700",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 18.0.2.1 (Oracle Corporation)"
)
public class CategoryMapperImpl implements CategoryMapper {

    @Override
    public CategoryEntity toModel(CategoryRequest request) {
        if ( request == null ) {
            return null;
        }

        CategoryEntity.CategoryEntityBuilder categoryEntity = CategoryEntity.builder();

        categoryEntity.name( request.getName() );

        return categoryEntity.build();
    }
}
