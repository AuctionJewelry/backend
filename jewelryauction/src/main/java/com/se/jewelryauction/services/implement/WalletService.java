package com.se.jewelryauction.services.implement;

import com.se.jewelryauction.components.securities.UserPrincipal;
import com.se.jewelryauction.models.AuctionEntity;
import com.se.jewelryauction.models.UserEntity;
import com.se.jewelryauction.models.WalletEntity;
import com.se.jewelryauction.models.enums.AuctionStatus;
import com.se.jewelryauction.repositories.IAuctionRepository;
import com.se.jewelryauction.repositories.IWalletRepository;
import com.se.jewelryauction.responses.WalletResponse;
import com.se.jewelryauction.services.IWalletService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class WalletService implements IWalletService {
    private final IWalletRepository walletRepository;
    private final IAuctionRepository auctionRepository;

    @Override
    public WalletResponse getWallet(){
        WalletResponse walletResponse = new WalletResponse();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UserEntity user = userPrincipal.getUser();
        List<AuctionEntity> auctionWin = auctionRepository.findByWinnerAndStatus(user, AuctionStatus.InProgress);
        float totalCurrentPrice = 0.0f;

        for (AuctionEntity auction : auctionWin) {
            totalCurrentPrice += auction.getCurrentPrice();
        }

        WalletEntity wallet = walletRepository.findByUser(user);
        walletResponse.setAvailable_money(wallet.getMoney()-totalCurrentPrice);
        walletResponse.setMoney(wallet.getMoney());
        return walletResponse;

    }





}