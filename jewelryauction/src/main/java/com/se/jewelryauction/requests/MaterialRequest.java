package com.se.jewelryauction.requests;

import jakarta.validation.constraints.NotEmpty;

public class MaterialRequest {
    @NotEmpty(message = "Material name cannot be empty")
    private String name;
}
