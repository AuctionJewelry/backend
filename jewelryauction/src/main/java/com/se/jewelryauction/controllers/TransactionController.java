package com.se.jewelryauction.controllers;

import com.se.jewelryauction.models.SystemTransactionEntity;
import com.se.jewelryauction.services.ITransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${app.api.version.v1}/transaction")
@RequiredArgsConstructor
public class TransactionController {
    private final ITransactionService systemTransactionService;

    @GetMapping("/user")
    public List<SystemTransactionEntity> getTransactionsByUserId() {
        return systemTransactionService.getTransactionsByUserId();
    }
    @GetMapping("")
    public List<SystemTransactionEntity> getTransactions() {
        return systemTransactionService.getTransactionsList();
    }

}
