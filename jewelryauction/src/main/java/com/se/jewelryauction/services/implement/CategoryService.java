package com.se.jewelryauction.services.implement;

import com.se.jewelryauction.components.exceptions.AppException;
import com.se.jewelryauction.models.CategoryEntity;
import com.se.jewelryauction.repositories.ICategoryRepository;
import com.se.jewelryauction.services.ICategoryService;
import com.se.jewelryauction.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {
    private final ICategoryRepository categoryRepository;

    @Override
    public CategoryEntity createCategory(CategoryEntity category) {
        category.setName(StringUtils.NameStandardlizing(category.getName()));
        if(categoryRepository.existsByName(category.getName())){
            throw new AppException(HttpStatus.BAD_REQUEST, "That name is existed!");
        }
        return categoryRepository.save(category);
    }

    @Override
    public CategoryEntity getCategoryById(long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "This categod is not existed!"));
    }

    @Override
    public List<CategoryEntity> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public CategoryEntity updateCategory(long materialId, CategoryEntity category) {
        category.setName(StringUtils.NameStandardlizing(category.getName()));
        if(categoryRepository.existsByName(category.getName())){
            throw new AppException(HttpStatus.BAD_REQUEST, "That name is existed!");
        }
        CategoryEntity existingCategory = this.getCategoryById(materialId);
        existingCategory.setName(category.getName());
        return categoryRepository.save(existingCategory);
    }

    @Override
    public void deleteCategory(long id) {
        CategoryEntity existingCategory = this.getCategoryById(id);
        categoryRepository.delete(existingCategory);
    }
}
