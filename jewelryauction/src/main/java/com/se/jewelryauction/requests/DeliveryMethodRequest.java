package com.se.jewelryauction.requests;

import com.se.jewelryauction.models.JewelryEntity;
import com.se.jewelryauction.models.UserEntity;
import com.se.jewelryauction.models.enums.DeliveryStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryMethodRequest {
    @NotNull(message = "User ID cannot be null")
    @Min(value = 1, message = "User ID must be greater than or equal to 1")
    private int userId;

    @NotNull(message = "Jewelry ID cannot be null")
    @Min(value = 1, message = "Jewelry ID must be greater than or equal to 1")
    private int jewelryId;

    @NotNull(message = "Full name cannot be null")
    private String full_name;

    @NotNull(message = "Phone number cannot be null")
    private String phone_number;

    @NotNull(message = "Address cannot be null")
    private String address;
}
