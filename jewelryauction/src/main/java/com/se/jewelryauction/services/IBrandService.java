package com.se.jewelryauction.services;

import com.se.jewelryauction.models.BrandEntity;

import java.util.List;

public interface IBrandService {
    BrandEntity createBrand(BrandEntity brand) ;
    BrandEntity   getBrandById(long id);
    List<BrandEntity> getAllBrands();
    BrandEntity updateBrand(long brandId, BrandEntity category);
    void deleteBrand(long id);
}
