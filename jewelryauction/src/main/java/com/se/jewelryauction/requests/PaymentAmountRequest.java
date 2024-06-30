package com.se.jewelryauction.requests;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentAmountRequest {

    private float amount;
    private float bank;
}
