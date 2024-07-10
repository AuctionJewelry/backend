package com.se.jewelryauction.services.implement;

import com.se.jewelryauction.components.securities.UserPrincipal;
import com.se.jewelryauction.models.SystemTransactionEntity;
import com.se.jewelryauction.models.UserEntity;
import com.se.jewelryauction.repositories.ISystemTransactionRepository;
import com.se.jewelryauction.services.ITransactionService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class TransactionService implements ITransactionService {
    private final ISystemTransactionRepository transactionRepository;

    @Override
    public List<SystemTransactionEntity> getTransactionsByUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UserEntity user = userPrincipal.getUser();
        return transactionRepository.findByUserId(user.getId());
    }
    @Override
    public List<SystemTransactionEntity> getTransactionsList() {
        return transactionRepository.findAll();
    }


}
