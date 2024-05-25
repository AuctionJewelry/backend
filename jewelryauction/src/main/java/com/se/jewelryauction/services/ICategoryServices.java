package com.se.jewelryauction.services;


import com.se.jewelryauction.models.CategoryEntity;

import java.util.List;

public interface ICategoryServices {
    CategoryEntity createCategory(CategoryEntity category) ;
    CategoryEntity   getCategoryById(long id);
    List<CategoryEntity> getAllCategories();
    CategoryEntity updateCategory(long materialId, CategoryEntity category);
    void deleteCategory(long id);
}
