package com.se.jewelryauction.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.se.jewelryauction.models.enums.PaymentForType;
import com.se.jewelryauction.models.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Payment extends BaseEntiy{
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @ManyToOne
    @JoinColumn(name = "wallet_id")
    private WalletEntity wallet;

    private float amount;

    private String bankCode;

    private String BankTranNo;

    private String CardType;

    private String PayDate;

    private String TransactionNo;

    private String TransactionStatus;
    private String FullName;



    @Column(name = "payment_for_type")
    @Enumerated(EnumType.STRING)
    private PaymentForType payment;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    public Payment(LocalDateTime createdAt, LocalDateTime updatedAt, String id, WalletEntity wallet, float amount, PaymentForType payment, String bankCode, PaymentStatus status) {
        super(createdAt, updatedAt);
        this.id = id;
        this.wallet = wallet;
        this.amount = amount;
        this.payment = payment;
        this.bankCode = bankCode;
        this.status = status;
    }

    public Payment(LocalDateTime createdAt, LocalDateTime updatedAt, String id, WalletEntity wallet, float amount, String bankCode, String BankTranNo, String FullName){
        super(createdAt, updatedAt);
        this.id = id;
        this.wallet = wallet;
        this.amount = amount;
        this.bankCode = bankCode;
        this.BankTranNo = BankTranNo;
        this.FullName = FullName;
        this.payment = PaymentForType.REFUND;
        this.status = PaymentStatus.PENDING;
    }
}
