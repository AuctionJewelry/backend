package com.se.jewelryauction.requests;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
import java.util.Map;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MaterialsRequest {
    private Long materialID;

    private float price;
}
