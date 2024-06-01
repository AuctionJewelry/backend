package com.se.jewelryauction.requests;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JewelryRequest {
    private String name;

    private String description;

    private Long category;

    private float weight;

    private String size;

    private String color;

    private String sex;

    private String brand;

    private String jewelryCondition;

    private String collection;

    private List<Long> materials;



}
