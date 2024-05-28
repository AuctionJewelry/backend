package com.se.jewelryauction.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MaterialRequest {
    @NotBlank(message = "Material name cannot be empty")
    private String name;
}
