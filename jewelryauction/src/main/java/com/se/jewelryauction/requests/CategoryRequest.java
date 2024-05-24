package com.se.jewelryauction.requests;

import jakarta.validation.constraints.NotBlank;

public class CategoryRequest {
    @NotBlank(message = "Name can not be empty")
    private String name;
}
