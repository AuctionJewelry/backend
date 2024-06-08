package com.se.jewelryauction.requests;

import com.se.jewelryauction.models.JewelryEntity;
import com.se.jewelryauction.models.UserEntity;
import com.se.jewelryauction.models.enums.PaymentMethod;
import com.se.jewelryauction.models.enums.ValuatingMethod;
import com.se.jewelryauction.models.enums.ValuatingStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateValuatingRequest {
    private String address;
    private int staffId;
    private float valuation_value;
    private String notes;
    private ValuatingStatus status;
    private float desiredPrice;
    private PaymentMethod paymentMethod;
    private ValuatingMethod valuatingMethod;

}
