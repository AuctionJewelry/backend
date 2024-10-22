package com.se.jewelryauction.controllers;

import com.se.jewelryauction.components.apis.CoreApiResponse;
import com.se.jewelryauction.models.SystemWalletEntity;
import com.se.jewelryauction.models.WalletEntity;
import com.se.jewelryauction.responses.WalletResponse;
import com.se.jewelryauction.services.IWalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${app.api.version.v1}/wallet")
@RequiredArgsConstructor
public class WalletController {
    private final IWalletService walletService;
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/details")
    public WalletResponse getWallet() {
        return walletService.getWallet();
    }

    @GetMapping("/system")
    public CoreApiResponse<SystemWalletEntity> getWalletSystem() {
        return CoreApiResponse.success(walletService.getWalletSystem());
    }

    @PostMapping("/deposit")
    public CoreApiResponse<WalletEntity> getWalletSystem(@RequestParam float amount){
        return CoreApiResponse.success(walletService.deposit(amount));

    }
}
