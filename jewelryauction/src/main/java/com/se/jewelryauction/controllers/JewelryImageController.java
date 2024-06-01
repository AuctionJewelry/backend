package com.se.jewelryauction.controllers;

import com.se.jewelryauction.components.apis.CoreApiResponse;
import com.se.jewelryauction.models.JewelryImageEntity;
import com.se.jewelryauction.responses.JewelryImageResponse;
import com.se.jewelryauction.services.IJewelryImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import static com.se.jewelryauction.mappers.JewelryImageMapper.INSTANCE;
@RestController
@RequestMapping("${app.api.version.v1}/jewelryImage")
@RequiredArgsConstructor
public class JewelryImageController {
    private final IJewelryImageService jewelryImageService;
    @PostMapping("/{id}")
    public CoreApiResponse<List<JewelryImageEntity>> uploadImages(
            @PathVariable Long id,
            @RequestParam(name = "files") List<MultipartFile> images) throws IOException {
        List<JewelryImageEntity> jewelryImages = jewelryImageService.createImageList(id, images);
        return CoreApiResponse.success(jewelryImages,"Jewelry image uploaded successfully");
    }

    @GetMapping("/bird/{birdId}")
    public CoreApiResponse<List<JewelryImageResponse>> getAllImageByBirdId(@PathVariable Long birdId) {
        List<JewelryImageEntity> images = jewelryImageService.getAllImages(birdId);
        return CoreApiResponse.success(INSTANCE.toListResponse(images));

    }

    @GetMapping("/{imageId}")
    public CoreApiResponse<JewelryImageResponse> getImage(@PathVariable Long imageId) {
        JewelryImageEntity image = jewelryImageService.getImage(imageId);
        return CoreApiResponse.success(INSTANCE.toResponse(image));

    }

    @DeleteMapping("/{imageId}")
    public CoreApiResponse<?> deleteImage(@PathVariable Long imageId) {

        jewelryImageService.deleteImage(imageId);
        return CoreApiResponse.success("Delete image successfully");

    }

}
