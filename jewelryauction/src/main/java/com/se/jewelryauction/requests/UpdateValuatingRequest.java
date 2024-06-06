package com.se.jewelryauction.requests;

import com.se.jewelryauction.models.JewelryEntity;
import com.se.jewelryauction.models.UserEntity;
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
    @NotNull(message = "Jewelry ID cannot be null")
    @Min(value = 1, message = "Jewelry ID must be greater than or equal to 1")
    private int jewelryId;

    @NotNull(message = "isOnline is required!")
    private boolean isOnline;

    @NotNull(message = "Staff ID cannot be null")
    @Min(value = 1, message = "Staff ID must be greater than or equal to 1")
    private int staffId;
    private float valuation_value;
    private String notes;
    private ValuatingStatus status;
    private float desiredPrice;
    private String paymentMethod;
    private float valuatingFee;
    private String valuatingMethod;
}
