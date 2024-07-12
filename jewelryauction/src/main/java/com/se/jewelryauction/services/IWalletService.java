package com.se.jewelryauction.services;

import com.se.jewelryauction.models.SystemWalletEntity;
import com.se.jewelryauction.responses.WalletResponse;

public interface IWalletService {
    WalletResponse getWallet();

    public SystemWalletEntity getWalletSystem();
}
