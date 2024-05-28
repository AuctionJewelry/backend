package com.se.jewelryauction.services.implement;

import com.se.jewelryauction.components.exceptions.AppException;
import com.se.jewelryauction.components.exceptions.DataNotFoundException;
import com.se.jewelryauction.models.CategoryEntity;
import com.se.jewelryauction.models.JewelryEntity;
import com.se.jewelryauction.repositories.*;
import com.se.jewelryauction.services.IJewelryServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class JewelryService implements IJewelryServices {
    private final IJewelryRepository jewelryRepository;
    private final IUserRepository userRepository;
    private final ICategoryRepository categoryRepository;
    private final IBrandRepository brandRepository;
    private final ICollectionRepository collectionRepository;
    @Override
    public JewelryEntity createJewelry(JewelryEntity jewelry) {
        CategoryEntity existingCategory = categoryRepository
                .findById(jewelry.getCategory().getId())
                .orElseThrow(() ->
                        new DataNotFoundException(
                                "Category", "id", jewelry.getCategory().getId()));
        return null;
    }

    @Override
    public JewelryEntity getJewelryById(long id) {
        return jewelryRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Cannot find jewelry with id: " + id));
    }

    @Override
    public List<JewelryEntity> getAllMaterials() {
        return jewelryRepository.findAll();
    }

    @Override
    public JewelryEntity updateJewelry(long jewelryId, JewelryEntity jewelry) {
        return null;
    }

    @Override
    public void deleteJewelry(long id) {
        JewelryEntity existingMaterial = getJewelryById(id);
        jewelryRepository.deleteById(id);
    }
}
