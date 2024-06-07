package com.se.jewelryauction.services.implement;

import com.se.jewelryauction.components.exceptions.AppException;
import com.se.jewelryauction.components.securities.UserPrincipal;
import com.se.jewelryauction.models.UserEntity;
import com.se.jewelryauction.models.WishListEntity;
import com.se.jewelryauction.repositories.IWishListRepository;
import com.se.jewelryauction.services.IWishListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class WishListService implements IWishListService {
    private final IWishListRepository wishListRepository;
    @Override
    public WishListEntity createWishList(WishListEntity wishList) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UserEntity user = userPrincipal.getUser();
        wishList.setUserId(user);
        return wishListRepository.save(wishList);
    }

    @Override
    public List<WishListEntity> getAllWishList() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UserEntity user = userPrincipal.getUser();
        return wishListRepository.findByUserId(user.getId());
    }

    @Override
    public void deleteWishList(long id) {
       WishListEntity wishList = wishListRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "Cannot find wishlist with id: " + id));
       wishListRepository.deleteById(id);

    }
}
