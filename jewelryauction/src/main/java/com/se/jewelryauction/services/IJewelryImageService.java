package com.se.jewelryauction.services;

import com.se.jewelryauction.models.JewelryImageEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IJewelryImageService {
    List<JewelryImageEntity> createImageList(Long jewelryId, List<MultipartFile> file) throws IOException;

    void deleteImage(long imageId);

    List<JewelryImageEntity> getAllImages(long jewelryId);

    JewelryImageEntity getImage(long imageId);

}
