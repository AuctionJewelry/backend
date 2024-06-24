package com.se.jewelryauction.services.implement;

import com.se.jewelryauction.components.constants.ImageContants;
import com.se.jewelryauction.components.exceptions.AppException;
import com.se.jewelryauction.components.exceptions.DataNotFoundException;
import com.se.jewelryauction.components.securities.UserPrincipal;
import com.se.jewelryauction.mappers.JewelryMapper;
import com.se.jewelryauction.models.*;
import com.se.jewelryauction.models.enums.JewelryStatus;
import com.se.jewelryauction.repositories.*;
import com.se.jewelryauction.requests.JewelryMaterialRequest;
import com.se.jewelryauction.requests.JewelryRequest;
import com.se.jewelryauction.services.IJewelryService;
import com.se.jewelryauction.utils.StringUtils;
import com.se.jewelryauction.utils.UploadImagesUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@Service
@RequiredArgsConstructor
public class JewelryService implements IJewelryService {
    private final IJewelryRepository jewelryRepository;
    private final ICategoryRepository categoryRepository;
    private final IBrandRepository brandRepository;
    private final ICollectionRepository collectionRepository;
    private final IMaterialRepository materialRepository;
    private final JewelryMapper jewelryMapper;
    private final IJewelryMaterialRepository jewelryMaterialRepository;
    @Override
    public JewelryEntity createJewelry(JewelryEntity jewelry, MultipartFile imageFile, List<MultipartFile> images) throws IOException {
        CategoryEntity existingCategory = categoryRepository
                .findById(jewelry.getCategory().getId())
                .orElseThrow(() ->
                        new DataNotFoundException(
                                "Category", "id", jewelry.getCategory().getId()));

        BrandEntity existingBrand = brandRepository
                .findByName(jewelry.getBrand().getName());
        if (existingBrand == null) {
            BrandEntity newBrand = new BrandEntity();
            newBrand.setName(jewelry.getBrand().getName());
            brandRepository.save(newBrand);
            existingBrand = newBrand;
        }
        CollectionEntity existingCollection = collectionRepository.
                findByName(jewelry.getCollection().getName());
        if (existingCollection == null) {
            CollectionEntity newCollection = new CollectionEntity();
            newCollection.setName(jewelry.getCollection().getName());
            newCollection.setBrand(existingBrand);
            collectionRepository.save(newCollection);
            existingCollection = newCollection;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UserEntity user = userPrincipal.getUser();

        jewelry.setName(StringUtils.NameStandardlizing(jewelry.getName()));
        jewelry.setCategory(existingCategory);
        jewelry.setBrand(existingBrand);
        jewelry.setCollection(existingCollection);
        jewelry.setSellerId(user);
        jewelry.setStatus(JewelryStatus.PENDING);

        if (imageFile != null && !imageFile.isEmpty()) {
            jewelry.setThumbnail(UploadImagesUtils.storeFile(imageFile, ImageContants.JEWELRY_IMAGE_PATH));
        }

        if (images != null && !images.isEmpty()) {
            List<JewelryImageEntity> birdImagesList = new ArrayList<>();
            for (MultipartFile image : images) {
                if (!image.isEmpty()) {
                    JewelryImageEntity jewelryImage = new JewelryImageEntity();
                    jewelryImage.setJewelry(jewelry);
                    jewelryImage.setUrl(UploadImagesUtils.storeFile(image, ImageContants.JEWELRY_IMAGE_PATH));
                    birdImagesList.add(jewelryImage);
                }
            }
            jewelry.setJewelryImages(birdImagesList);
        }
        List<JewelryMaterialEntity> jewelryMaterialList = jewelry.getJewelryMaterials();
        List<JewelryMaterialEntity> newJewelryMaterialList = new ArrayList<>();
        for (JewelryMaterialEntity material : jewelryMaterialList) {
            MaterialEntity materialEntity = materialRepository.findById(material.getMaterial().getId())
                    .orElseThrow(() -> new DataNotFoundException("Category", "id", material.getMaterial().getId()));
            JewelryMaterialEntity jewelryMaterialEntity = jewelryMaterialRepository.findByJewelryAndMaterial(jewelry, materialEntity);
            if(jewelryMaterialEntity != null){
                jewelryMaterialEntity.setWeight(jewelryMaterialEntity.getWeight() + material.getWeight());
            }
            else{
                jewelryMaterialEntity.setWeight(material.getWeight());
                jewelryMaterialEntity.setJewelry(jewelry);
                jewelryMaterialEntity.setMaterial(materialEntity);
            }
            newJewelryMaterialList.add(jewelryMaterialEntity);
        }
        jewelry.setJewelryMaterials(newJewelryMaterialList);
        JewelryEntity savedJewelry = jewelryRepository.save(jewelry);

        savedJewelry.getJewelryMaterials().forEach(jm -> {
            MaterialEntity fullMaterial = materialRepository.findById(jm.getMaterial().getId()).orElse(null);
            jm.setMaterial(fullMaterial);
        });

        return savedJewelry;
    }

    @Override
    public JewelryEntity getJewelryById(long id) {
        return jewelryRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Cannot find jewelry with id: " + id));
    }

    @Override
    public List<JewelryEntity> getAllJewelrys() {
        return jewelryRepository.findAll();
    }

    @Override
    public JewelryEntity updateJewelry(long jewelryId, JewelryRequest jewelry) {
        JewelryEntity existingJewelry = getJewelryById(jewelryId);
        jewelryMapper.updateJewelryFromRequest(jewelry, existingJewelry);
        if (jewelry.getCategory() != null && !existingJewelry.getCategory().getId().equals(jewelry.getCategory())) {
            CategoryEntity existingCategory = categoryRepository
                    .findById(jewelry.getCategory())
                    .orElseThrow(() ->
                            new DataNotFoundException(
                                    "Category", "id", jewelry.getCategory()));
            existingJewelry.setCategory(existingCategory);
        }

        if (jewelry.getBrand() != null && !existingJewelry.getBrand().getName().equals(jewelry.getBrand())) {
            BrandEntity existingBrand = brandRepository
                    .findByName(jewelry.getBrand());
            if (existingBrand == null) {
                BrandEntity newBrand = new BrandEntity();
                newBrand.setName(existingJewelry.getBrand().getName());
                brandRepository.save(newBrand);
                existingBrand = newBrand;
            }

            existingJewelry.setBrand(existingBrand);
        }


        if (jewelry.getCollection() != null && !existingJewelry.getCollection().getName().equals(jewelry.getCollection())) {
            CollectionEntity existingCollection = collectionRepository
                    .findByName(jewelry.getCollection());
            if (existingCollection == null) {
                CollectionEntity newCollection = new CollectionEntity();
                newCollection.setName(existingJewelry.getCollection().getName());
                newCollection.setBrand(existingJewelry.getBrand());
                collectionRepository.save(newCollection);
                existingCollection = newCollection;
            }

            existingJewelry.setCollection(existingCollection);
        }
        if (jewelry.getMaterials() != null) {
            boolean materialsChanged = false;

            List<JewelryMaterialRequest> jewelryMaterialRequests = jewelry.getMaterials();
            List<JewelryMaterialEntity> existingJewelryMaterialList = existingJewelry.getJewelryMaterials();

            if (jewelryMaterialRequests.size() != existingJewelryMaterialList.size()) {
                materialsChanged = true;
            } else {
                for (int i = 0; i < jewelryMaterialRequests.size(); i++) {
                    JewelryMaterialRequest newMaterialRequest = jewelryMaterialRequests.get(i);
                    JewelryMaterialEntity existingMaterial = existingJewelryMaterialList.get(i);

                    if (!newMaterialRequest.getIdMaterial().equals(existingMaterial.getMaterial().getId()) ||
                            !newMaterialRequest.getWeight().equals(existingMaterial.getWeight())) {
                        materialsChanged = true;
                        break;
                    }
                }
            }

            if (materialsChanged) {
                List<JewelryMaterialEntity> newJewelryMaterialList = new ArrayList<>();
                for (JewelryMaterialRequest materialRequest : jewelryMaterialRequests) {
                    JewelryMaterialEntity jewelryMaterial = new JewelryMaterialEntity();
                    jewelryMaterial.setJewelry(existingJewelry);
                    jewelryMaterial.setWeight(materialRequest.getWeight());

                    MaterialEntity materialEntity = materialRepository.findById(materialRequest.getIdMaterial())
                            .orElseThrow(() -> new DataNotFoundException("Category", "id", materialRequest.getIdMaterial()));
                    jewelryMaterial.setMaterial(materialEntity);

                    newJewelryMaterialList.add(jewelryMaterial);
                }
                existingJewelry.setJewelryMaterials(newJewelryMaterialList);
            }
        }

        return  jewelryRepository.save(existingJewelry);
    }

    @Override
    public void uploadThumbnail(Long jewelryId, MultipartFile imageFile) throws IOException {
        JewelryEntity existingJewelry = getJewelryById(jewelryId);
        existingJewelry.setThumbnail(UploadImagesUtils.storeFile(imageFile, ImageContants.JEWELRY_IMAGE_PATH));
        jewelryRepository.save(existingJewelry);
    }


    @Override
    public void deleteJewelry(long id) {
        JewelryEntity existingJewelry = getJewelryById(id);
        jewelryRepository.deleteById(id);
    }

    @Override
    public List<JewelryEntity> getJewelryBySellerId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UserEntity user = userPrincipal.getUser();
        return jewelryRepository.findBySellerId(user.getId());
    }
}
