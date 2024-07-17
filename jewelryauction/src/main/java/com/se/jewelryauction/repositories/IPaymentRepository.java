package com.se.jewelryauction.repositories;

import com.se.jewelryauction.models.Payment;
import com.se.jewelryauction.models.WalletEntity;
import com.se.jewelryauction.models.enums.PaymentForType;
import com.se.jewelryauction.models.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IPaymentRepository extends JpaRepository<Payment, String> {
    Payment findPaymentById(String id);
    List<Payment> findByPaymentAndStatusAndWalletId(
            PaymentForType paymentForType,
            PaymentStatus status,
            Long walletId
    );

    @Query("SELECT p FROM Payment p WHERE p.wallet.user.id = :userId")
    List<Payment> findByUserId(Long userId);

}
