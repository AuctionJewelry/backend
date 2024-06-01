package com.se.jewelryauction.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CollectionRequest {
    @NotBlank(message = "Name can not be empty")
    private String name;

    @NotBlank(message = "Brand can not be empty")
    private String brand;
}
