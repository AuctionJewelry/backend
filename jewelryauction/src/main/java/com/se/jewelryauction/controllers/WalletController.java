package com.se.jewelryauction.controllers;

import com.se.jewelryauction.responses.WalletResponse;
import com.se.jewelryauction.services.IWalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
