package com.se.jewelryauction.requests;

import jakarta.validation.Valid;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreatingValuatingRequest {
    @Valid
    private ValuatingRequest request;

    @Valid
    private MaterialsRequest materialsRequest;
}
