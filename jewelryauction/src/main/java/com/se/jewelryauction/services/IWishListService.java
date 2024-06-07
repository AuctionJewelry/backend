package com.se.jewelryauction.services;

import com.se.jewelryauction.models.WishListEntity;

import java.util.List;

public interface IWishListService {
    WishListEntity createWishList(WishListEntity wishList) ;
    List<WishListEntity> getAllWishList();
    void deleteWishList(long id);
}
