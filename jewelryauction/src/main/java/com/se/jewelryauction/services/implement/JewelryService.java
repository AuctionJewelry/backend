package com.se.jewelryauction.services.implement;

import com.se.jewelryauction.components.constants.ImageContants;
import com.se.jewelryauction.components.exceptions.AppException;
import com.se.jewelryauction.components.exceptions.DataNotFoundException;
import com.se.jewelryauction.components.securities.UserPrincipal;
import com.se.jewelryauction.mappers.JewelryMapper;
import com.se.jewelryauction.models.*;
import com.se.jewelryauction.models.enums.DeliveryStatus;
import com.se.jewelryauction.models.enums.JewelryStatus;
import com.se.jewelryauction.repositories.*;
import com.se.jewelryauction.requests.JewelryMaterialRequest;
import com.se.jewelryauction.requests.JewelryRequest;
import com.se.jewelryauction.requests.RefundJewelryRequest;
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
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JewelryService implements IJewelryService {
    private final IJewelryRepository jewelryRepository;
    private final ICategoryRepository categoryRepository;
    private final IBrandRepository brandRepository;
    private final ICollectionRepository collectionRepository;
    private final IMaterialRepository materialRepository;
    private final IDeliveryMethodRepository deliveryMethodRepository;
    private final JewelryMapper jewelryMapper;
    @Override
    public JewelryEntity createJewelry(JewelryEntity jewelry, MultipartFile imageFile, List<MultipartFile> images) throws IOException {
        CategoryEntity existingCategory = categoryRepository
                .findById(jewelry.getCategory().getId())
                .orElseThrow(() ->
                        new DataNotFoundException(
                                "Category", "id", jewelry.getCategory().getId()));

        BrandEntity existingBrand = brandRepository
                .findByName(jewelry.getBrand().getName());


        CollectionEntity existingCollection = collectionRepository
                .findByName(jewelry.getCollection().getName());


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
            JewelryMaterialEntity jewelryMaterial = new JewelryMaterialEntity();
            jewelryMaterial.setJewelry(jewelry);
            jewelryMaterial.setWeight(material.getWeight());
            MaterialEntity materialEntity = materialRepository.findById(material.getMaterial().getId())
                    .orElseThrow(() -> new DataNotFoundException("Category", "id", material.getMaterial().getId()));
            jewelryMaterial.setMaterial(materialEntity);
            newJewelryMaterialList.add(jewelryMaterial);
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
                    .orElseThrow(() -> new DataNotFoundException("Category", "id", jewelry.getCategory()));
            existingJewelry.setCategory(existingCategory);
        }

        if (jewelry.getBrand() != null && !existingJewelry.getBrand().getName().equals(jewelry.getBrand())) {
            BrandEntity existingBrand = brandRepository
                    .findByName(jewelry.getBrand());
            existingJewelry.setBrand(existingBrand);
        }

        if (jewelry.getCollection() != null && !existingJewelry.getCollection().getName().equals(jewelry.getCollection())) {
            CollectionEntity existingCollection = collectionRepository
                    .findByName(jewelry.getCollection());
            existingJewelry.setCollection(existingCollection);
        }

        return jewelryRepository.save(existingJewelry);
    }


    @Override
    public JewelryEntity updateJewelryMaterials(long jewelryId, List<JewelryMaterialRequest> materialsUpdateRequests) {
        JewelryEntity existingJewelry = getJewelryById(jewelryId);

        // Update existing materials
        for (JewelryMaterialRequest materialRequest : materialsUpdateRequests) {
            Long materialId = materialRequest.getIdMaterial();
            Float weight = materialRequest.getWeight();

            // Check if the material exists in the jewelry's materials list
            Optional<JewelryMaterialEntity> optionalJewelryMaterial = existingJewelry.getJewelryMaterials().stream()
                    .filter(jm -> jm.getMaterial().getId().equals(materialId))
                    .findFirst();

            if (optionalJewelryMaterial.isPresent()) {
                JewelryMaterialEntity jewelryMaterial = optionalJewelryMaterial.get();
                jewelryMaterial.setWeight(weight);
            } else {
                // Material not found, add new JewelryMaterialEntity
                MaterialEntity materialEntity = materialRepository.findById(materialId)
                        .orElseThrow(() -> new DataNotFoundException("Material", "id", materialId));

                JewelryMaterialEntity newJewelryMaterial = new JewelryMaterialEntity();
                newJewelryMaterial.setJewelry(existingJewelry);
                newJewelryMaterial.setMaterial(materialEntity);
                newJewelryMaterial.setWeight(weight);

                existingJewelry.getJewelryMaterials().add(newJewelryMaterial);
            }
        }

        return jewelryRepository.save(existingJewelry);
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
    @Override
    public DeliveryMethodEntity refundJewelry(RefundJewelryRequest jewelryRequest){
        DeliveryMethodEntity deliveryMethod = new DeliveryMethodEntity();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UserEntity user = userPrincipal.getUser();
        JewelryEntity jewelry= jewelryRepository.findById(jewelryRequest.getJewelryId())
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Cannot find jewelry with id: " + jewelryRequest.getJewelryId()));
        if (!jewelry.getSellerId().getId().equals(user.getId())) {
            throw new AppException(HttpStatus.UNAUTHORIZED, "Bạn không có quyền truy cập");
        }
        deliveryMethod.setUser(user);
        deliveryMethod.setAddress(jewelryRequest.getAddress());
        deliveryMethod.setFull_name(jewelryRequest.getFull_name());
        deliveryMethod.setPhone_number(jewelryRequest.getPhone_number());
        deliveryMethod.setJewelry(jewelry);
        deliveryMethod.setStatus(DeliveryStatus.PREPARING);
        deliveryMethod.setValuatingDelivery(false);
        jewelry.setStatus(JewelryStatus.REFUNDING);
        jewelryRepository.save(jewelry);

        return deliveryMethodRepository.save(deliveryMethod);
    }
    @Override
    public DeliveryMethodEntity comfirmRefund(long id) {

        DeliveryMethodEntity deliveryAuction = deliveryMethodRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Can not find the delivery auction"));
        if (deliveryAuction.getStatus() == DeliveryStatus.RECEIVED) {
            throw new AppException(HttpStatus.BAD_REQUEST, "You have confirmed your delivery ");
        }
        deliveryAuction.setStatus(DeliveryStatus.RECEIVED);
        return deliveryMethodRepository.save(deliveryAuction);
    }

}
