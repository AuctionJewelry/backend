package com.se.jewelryauction.repositories;

import com.se.jewelryauction.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPaymentRepository extends JpaRepository<Payment, String> {
    Payment findPaymentById(String id);
}
