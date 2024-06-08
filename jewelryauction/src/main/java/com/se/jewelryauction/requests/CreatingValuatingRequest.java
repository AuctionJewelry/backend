package com.se.jewelryauction.requests;

import jakarta.validation.Valid;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreatingValuatingRequest {
    @Valid
    private ValuatingRequest request;

    @Valid
    private List<MaterialsRequest> materialsRequest;
}
