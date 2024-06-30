package com.se.jewelryauction.responses;

import com.se.jewelryauction.models.UserEntity;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ValuatingStaffResponse {
    private UserEntity user;
    private int valuatingCount;
}
