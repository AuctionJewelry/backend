package com.se.jewelryauction.services.implement;

import com.se.jewelryauction.components.constants.ImageContants;
import com.se.jewelryauction.components.exceptions.DataNotFoundException;
import com.se.jewelryauction.models.JewelryEntity;
import com.se.jewelryauction.models.JewelryImageEntity;
import com.se.jewelryauction.repositories.IJewelryImageRepository;
import com.se.jewelryauction.repositories.IJewelryRepository;
import com.se.jewelryauction.services.IJewelryImageService;
import com.se.jewelryauction.utils.UploadImagesUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@Service
@RequiredArgsConstructor
public class JewelryImageService implements IJewelryImageService {
    private final IJewelryRepository jewelryRepository;
    private final IJewelryImageRepository jewelryImageRepository;
    @Override
    public List<JewelryImageEntity> createImageList(Long jewelryId, List<MultipartFile> file) throws IOException {
        JewelryEntity existingProduct = jewelryRepository
                .findById(jewelryId)
                .orElseThrow(() ->
                        new DataNotFoundException(
                                "Bird","id",jewelryId));

        List<JewelryImageEntity> jewelryImages = new ArrayList<>();
        for (MultipartFile image : file) {
            if (!image.isEmpty()) {
                JewelryImageEntity jewelryImage = new JewelryImageEntity();
                jewelryImage.setJewelry(existingProduct);
                jewelryImage.setUrl(UploadImagesUtils.storeFile(image, ImageContants.JEWELRY_IMAGE_PATH));
                jewelryImages.add(jewelryImage);
                jewelryImageRepository.save(jewelryImage);
            }
        }
        return jewelryImages;
    }

    @Override
    public void deleteImage(long imageId) {
        JewelryImageEntity image = jewelryImageRepository.findById(imageId)
                .orElseThrow(() -> new DataNotFoundException("Image", "id", imageId));
        jewelryImageRepository.delete(image);
    }

    @Override
    public List<JewelryImageEntity> getAllImages(long jewelryId) {
        return jewelryImageRepository.findByJewelryId(jewelryId);
    }

    @Override
    public JewelryImageEntity getImage(long imageId) {
        return jewelryImageRepository.findById(imageId)
                .orElseThrow(() -> new DataNotFoundException("Image", "id", imageId));
    }
}
