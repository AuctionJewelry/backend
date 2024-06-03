package com.se.jewelryauction.requests;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JewelryMaterialRequest {

    private Long idMaterial;

    private Float weight;
}
