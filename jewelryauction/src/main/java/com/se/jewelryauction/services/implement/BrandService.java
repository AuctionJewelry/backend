package com.se.jewelryauction.services.implement;

import com.se.jewelryauction.components.exceptions.AppException;
import com.se.jewelryauction.models.BrandEntity;
import com.se.jewelryauction.models.CategoryEntity;
import com.se.jewelryauction.repositories.IBrandRepository;
import com.se.jewelryauction.repositories.ICategoryRepository;
import com.se.jewelryauction.services.IBrandService;
import com.se.jewelryauction.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class BrandService implements IBrandService {
    private final IBrandRepository brandRepository;

    @Override
    public BrandEntity createBrand(BrandEntity category) {
        category.setName(StringUtils.NameStandardlizing(category.getName()));
        if(brandRepository.existsByName(category.getName())){
            throw new AppException(HttpStatus.BAD_REQUEST, "That name is existed!");
        }
        return brandRepository.save(category);
    }

    @Override
    public BrandEntity getBrandById(long id) {
        return brandRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "This material is not existed!"));
    }

    @Override
    public List<BrandEntity> getAllBrands() {
        return brandRepository.findAll();
    }

    @Override
    public BrandEntity updateBrand(long materialId, BrandEntity brand) {
        brand.setName(StringUtils.NameStandardlizing(brand.getName()));
        if(brandRepository.existsByName(brand.getName())){
            throw new AppException(HttpStatus.BAD_REQUEST, "That name is existed!");
        }
        BrandEntity existingBrand = this.getBrandById(materialId);
        existingBrand.setName(brand.getName());
        return brandRepository.save(existingBrand);
    }

    @Override
    public void deleteBrand(long id) {
        BrandEntity existingBrand = getBrandById(id);
        brandRepository.delete(existingBrand);
    }
}
