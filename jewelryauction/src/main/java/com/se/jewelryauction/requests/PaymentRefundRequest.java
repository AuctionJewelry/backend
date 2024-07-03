package com.se.jewelryauction.requests;

import com.se.jewelryauction.models.enums.PaymentForType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRefundRequest {
    private float amount;

    private String bankCode;

    private String BankTranNo;

    private String FullName;
}
