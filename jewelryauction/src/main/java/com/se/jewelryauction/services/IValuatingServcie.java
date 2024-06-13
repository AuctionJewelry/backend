package com.se.jewelryauction.services;


import com.google.gson.JsonElement;
import com.se.jewelryauction.models.ValuatingEntity;
import com.se.jewelryauction.requests.MaterialsRequest;
import com.se.jewelryauction.services.implement.MaterialService;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;

public interface IValuatingServcie {
    ValuatingEntity createValuating(ValuatingEntity valuating) throws IOException, URISyntaxException;
    ValuatingEntity getValuatingById(long id);
    List<ValuatingEntity> getAllValuating();
    ValuatingEntity updateValuating(long valuatingId, ValuatingEntity valuating);
    void deleteValuating(long id);
    List<ValuatingEntity> getValuatingByJewelryId(long id);
    List<ValuatingEntity> getValuatingByCurrentUser();
    public float getCurrentPrice(String material) throws IOException, URISyntaxException;
}
