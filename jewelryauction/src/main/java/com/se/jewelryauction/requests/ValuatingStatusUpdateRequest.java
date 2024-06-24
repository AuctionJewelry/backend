package com.se.jewelryauction.requests;

import com.se.jewelryauction.models.enums.ValuatingStatus;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ValuatingStatusUpdateRequest {
    public ValuatingStatus status;
}
