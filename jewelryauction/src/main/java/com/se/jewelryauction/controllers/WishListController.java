package com.se.jewelryauction.controllers;

import com.se.jewelryauction.components.apis.CoreApiResponse;
import com.se.jewelryauction.models.WishListEntity;
import com.se.jewelryauction.requests.WishListRequest;
import com.se.jewelryauction.services.IWishListService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.se.jewelryauction.mappers.WishListMapper.INSTANCE;

@RestController
@RequestMapping("${app.api.version.v1}/wishlist")
@RequiredArgsConstructor
public class WishListController {
    private final IWishListService wishListService ;
    @PreAuthorize("hasRole('USER')")
    @PostMapping("")
    public CoreApiResponse<WishListEntity> createWishList(
            @Valid @RequestBody WishListRequest wishListRequest
    ){
        WishListEntity wishList = wishListService.createWishList(INSTANCE.toModel(wishListRequest));
        return CoreApiResponse.success(wishList,"Insert wish list successfully");
    }
    @GetMapping("")
    public CoreApiResponse<List<WishListEntity>> getWishList(){
        List<WishListEntity> wishList = wishListService.getAllWishList();
        return CoreApiResponse.success(wishList);
    }

    @DeleteMapping("/{id}")
    public CoreApiResponse<?> deleteWishList(
            @PathVariable Long id
    ){
        wishListService.deleteWishList(id);
        return CoreApiResponse.success("Delete wish list successfully");
    }
}
