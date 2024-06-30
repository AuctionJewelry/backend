package com.se.jewelryauction.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.se.jewelryauction.models.enums.PaymentMethod;
import com.se.jewelryauction.models.enums.ValuatingMethod;
import com.se.jewelryauction.models.enums.ValuatingStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ValuatingRequest {
    @NotNull(message = "Jewelry ID cannot be null")
    @Min(value = 1, message = "Jewelry ID must be greater than or equal to 1")
    private int jewelryId;

    @NotNull(message = "isOnline is required!")
    private boolean isOnline;

    private float desiredPrice;
    private PaymentMethod paymentMethod;
    private String notes;
    private ValuatingMethod valuatingMethod;
    private String address;
}
