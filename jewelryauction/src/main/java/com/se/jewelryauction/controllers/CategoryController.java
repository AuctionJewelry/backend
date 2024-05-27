package com.se.jewelryauction.controllers;

import com.se.jewelryauction.components.apis.CoreApiResponse;
import com.se.jewelryauction.models.CategoryEntity;
import com.se.jewelryauction.models.MaterialEntity;
import com.se.jewelryauction.requests.CategoryRequest;
import com.se.jewelryauction.requests.MaterialRequest;
import com.se.jewelryauction.services.ICategoryServices;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.se.jewelryauction.mappers.CategoryMapper.INSTANCE;

@RestController
@RequestMapping("${app.api.version.v1}/category")
@RequiredArgsConstructor
public class CategoryController {
    private final ICategoryServices categoryServices;

    @PostMapping("")
    public CoreApiResponse<CategoryEntity> createCategory(
            @Valid @RequestBody CategoryRequest categoryRequest
    ){
        CategoryEntity birdTypeResponse = categoryServices.createCategory(INSTANCE.toModel(categoryRequest));
        return CoreApiResponse.success(birdTypeResponse,"Insert material successfully");
    }

    @GetMapping("")
    public CoreApiResponse<List<CategoryEntity>> getAllCategories(){
        List<CategoryEntity> material = categoryServices.getAllCategories();
        return CoreApiResponse.success(material);
    }

    @GetMapping("/{id}")
    public CoreApiResponse<CategoryEntity> getCategoryById(@Valid @PathVariable Long id){
        CategoryEntity material = categoryServices.getCategoryById(id);
        return CoreApiResponse.success(material);
    }

    @PutMapping("/{id}")
    public CoreApiResponse<CategoryEntity> updateMaterial(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequest categoryRequest
    ){
        CategoryEntity updateCategory = categoryServices.updateCategory(id, INSTANCE.toModel(categoryRequest));
        return CoreApiResponse.success(updateCategory, "Update category successfully");
    }

    @DeleteMapping("/{id}")
    public CoreApiResponse<?> deleteCategory(
            @PathVariable Long id
    ){
        categoryServices.deleteCategory(id);
        return CoreApiResponse.success("Delete category successfully");
    }


}
