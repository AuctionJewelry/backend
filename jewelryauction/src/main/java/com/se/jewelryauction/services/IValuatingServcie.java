package com.se.jewelryauction.services;

import com.se.jewelryauction.models.ValuatingEntity;
import com.se.jewelryauction.requests.MaterialsRequest;
import com.se.jewelryauction.services.implement.MaterialService;

import java.util.List;

public interface IValuatingServcie {
    ValuatingEntity createValuating(ValuatingEntity valuating, List<MaterialsRequest> list) ;
    ValuatingEntity getValuatingById(long id);
    List<ValuatingEntity> getAllValuating();
    ValuatingEntity updateValuating(long valuatingId, ValuatingEntity valuating);
    void deleteValuating(long id);
}
