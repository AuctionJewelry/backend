package com.se.jewelryauction.repositories;

import com.se.jewelryauction.models.Payment;
import com.se.jewelryauction.models.WalletEntity;
import com.se.jewelryauction.models.enums.PaymentForType;
import com.se.jewelryauction.models.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IPaymentRepository extends JpaRepository<Payment, String> {
    Payment findPaymentById(String id);
    List<Payment> findByPaymentAndStatusAndWalletId(
            PaymentForType paymentForType,
            PaymentStatus status,
            Long walletId
    );

}
