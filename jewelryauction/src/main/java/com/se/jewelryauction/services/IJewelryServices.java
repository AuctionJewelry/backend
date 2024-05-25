package com.se.jewelryauction.services;


import com.se.jewelryauction.models.JewelryEntity;
import com.se.jewelryauction.models.MaterialEntity;

import java.util.List;

public interface IJewelryServices {
    JewelryEntity createJewelry(JewelryEntity jewelry) ;
    JewelryEntity getJewelryById(long id);
    List<JewelryEntity> getAllMaterials();
    JewelryEntity updateJewelry(long jewelryId, JewelryEntity jewelry);
    void deleteJewelry(long id);
}
