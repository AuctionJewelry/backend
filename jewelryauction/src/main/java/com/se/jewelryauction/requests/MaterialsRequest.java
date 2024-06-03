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
    @NotNull(message = "Materials cannot be null")
    Map<Long, Float> materials;
}
