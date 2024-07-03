package com.se.jewelryauction.services;

import com.se.jewelryauction.models.SystemTransactionEntity;

import java.util.List;

public interface ITransactionService {
    List<SystemTransactionEntity> getTransactionsByUserId();
}
