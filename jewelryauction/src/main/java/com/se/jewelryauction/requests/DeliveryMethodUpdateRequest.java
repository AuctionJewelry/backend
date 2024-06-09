package com.se.jewelryauction.requests;

import com.se.jewelryauction.models.UserEntity;
import com.se.jewelryauction.models.enums.DeliveryStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryMethodUpdateRequest {
    private int staffId;
    private String full_name;
    private String phone_number;
    private String address;
    private DeliveryStatus status;
}
