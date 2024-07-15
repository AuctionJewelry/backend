package com.se.jewelryauction.services;


import com.se.jewelryauction.models.ValuatingEntity;
import com.se.jewelryauction.models.enums.ValuatingStatus;
import com.se.jewelryauction.responses.ValuatingResponse;
import com.se.jewelryauction.responses.ValuatingStaffResponse;


import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;


public interface IValuatingServcie {
    ValuatingResponse createValuating(ValuatingEntity valuating) throws IOException, URISyntaxException;
    ValuatingEntity getValuatingById(long id);
    List<ValuatingEntity> getAllValuating();
    ValuatingEntity updateValuating(long valuatingId, ValuatingEntity valuating);
    ValuatingEntity updateStatusValuating(long valuatingId, ValuatingStatus valuatingStatus);
    void deleteValuating(long id);
    List<ValuatingEntity> getValuatingByJewelryId(long id);
    List<ValuatingEntity> getValuatingByCurrentUser();
    public float getCurrentPrice(String material) throws IOException, URISyntaxException;
    public List<ValuatingStaffResponse> getValuatingStaff();
    public ValuatingResponse reValuating(long id);
}
