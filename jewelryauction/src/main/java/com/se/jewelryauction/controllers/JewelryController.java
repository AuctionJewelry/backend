package com.se.jewelryauction.controllers;

import com.se.jewelryauction.components.apis.CoreApiResponse;
import com.se.jewelryauction.models.DeliveryMethodEntity;
import com.se.jewelryauction.models.JewelryEntity;
import com.se.jewelryauction.requests.CheckOutRequest;
import com.se.jewelryauction.requests.JewelryMaterialRequest;
import com.se.jewelryauction.requests.JewelryRequest;
import com.se.jewelryauction.requests.RefundJewelryRequest;
import com.se.jewelryauction.services.IJewelryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.se.jewelryauction.mappers.JewelryMapper.INSTANCE;

@RestController
@RequestMapping("${app.api.version.v1}/jewelry")
@RequiredArgsConstructor
public class JewelryController {
    private final IJewelryService jewelryService;

    @PostMapping("")
    @PreAuthorize("hasRole('USER')")
    public CoreApiResponse<JewelryEntity> createJewelry(
            @Valid @ModelAttribute JewelryRequest jewelryRequest,
            @RequestParam(name = "imageThumbnail", required = false) MultipartFile imageThumbnail,
            @RequestParam(name = "imagesFile", required = false) List<MultipartFile> images
    ) throws IOException {
        JewelryEntity jewelryResponse = jewelryService.createJewelry(INSTANCE.toModel(jewelryRequest),imageThumbnail,images);
        return CoreApiResponse.success(jewelryResponse,"Insert jewelry successfully");
    }

    @GetMapping("")
    public List<JewelryEntity> getAllJewelrys(){
        List<JewelryEntity> jewelrys = jewelryService.getAllJewelrys();
        return jewelryService.getAllJewelrys();
    }

    @GetMapping("/{id}")
    public JewelryEntity getJewelryById(@Valid @PathVariable Long id){
        JewelryEntity jewelry = jewelryService.getJewelryById(id);
        return jewelryService.getJewelryById(id);
    }

    @PutMapping("/{id}")
    public CoreApiResponse<JewelryEntity> updateMaterial(
            @PathVariable Long id,
            @RequestBody JewelryRequest jewelryRequest
    ){
        JewelryEntity updateJewelry= jewelryService.updateJewelry(id, jewelryRequest);
        return CoreApiResponse.success(updateJewelry, "Update jewelry successfully");
    }

    @PutMapping("/materials/{jewelryId}")
    public CoreApiResponse<JewelryEntity> updateJewelryMaterials(
            @PathVariable long jewelryId,
            @RequestBody List<JewelryMaterialRequest> materialsUpdateRequests) {
        JewelryEntity updatedJewelry = jewelryService.updateJewelryMaterials(jewelryId, materialsUpdateRequests);
        return CoreApiResponse.success(updatedJewelry);
    }

    @DeleteMapping("/{id}")
    public CoreApiResponse<?> deleteCategory(
            @PathVariable Long id
    ){
        jewelryService.deleteJewelry(id);
        return CoreApiResponse.success("Delete jewelry successfully");
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public List<JewelryEntity> getJewelryBySellerId() {
        return jewelryService.getJewelryBySellerId();
    }

    @PostMapping("/refund")
    public CoreApiResponse<DeliveryMethodEntity> createJewelry(@RequestBody RefundJewelryRequest request){
        DeliveryMethodEntity deliveryMethod;
        deliveryMethod = jewelryService.refundJewelry(request);
        return  CoreApiResponse.success(deliveryMethod,"Create refund jewelry request successfully");
    }

    @PutMapping("/refund/confirm/{id}")
    public CoreApiResponse<DeliveryMethodEntity> confirmDelivery(@PathVariable long id) {
        DeliveryMethodEntity deliveryMethod = jewelryService.comfirmRefund(id);
        return CoreApiResponse.success(deliveryMethod, "Delivery confirmed successfully");
    }


}
