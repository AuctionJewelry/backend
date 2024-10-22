package com.se.jewelryauction.services;

import com.se.jewelryauction.models.SystemWalletEntity;
import com.se.jewelryauction.models.WalletEntity;
import com.se.jewelryauction.responses.WalletResponse;

public interface IWalletService {
    WalletResponse getWallet();

    public SystemWalletEntity getWalletSystem();

    WalletEntity deposit (float amount);
}
