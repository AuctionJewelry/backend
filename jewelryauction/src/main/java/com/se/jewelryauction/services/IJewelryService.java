package com.se.jewelryauction.services;


import com.se.jewelryauction.models.JewelryEntity;
import com.se.jewelryauction.requests.JewelryMaterialRequest;
import com.se.jewelryauction.requests.JewelryRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IJewelryService {
    JewelryEntity createJewelry(JewelryEntity jewelry, MultipartFile imageFile,List<MultipartFile> images) throws IOException;
    JewelryEntity getJewelryById(long id);
    List<JewelryEntity> getAllJewelrys();
    JewelryEntity updateJewelry(long jewelryId, JewelryRequest jewelry);
    void deleteJewelry(long id);
    void uploadThumbnail(Long jewelryId, MultipartFile imageFile) throws IOException;

    List<JewelryEntity> getJewelryBySellerId();

    JewelryEntity updateJewelryMaterials(long jewelryId, List<JewelryMaterialRequest> materialsUpdateRequests);


}
