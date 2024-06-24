package com.se.jewelryauction.responses;

import com.se.jewelryauction.models.MaterialEntity;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ValuatingPerMaterialResponse {
    public MaterialEntity material;
    public float weight;
    public float price;
    public float sum;

    public ValuatingPerMaterialResponse(MaterialEntity material, float weight, float price) {
        this.material = material;
        this.weight = weight;
        this.price = price;
        this.sum = price * weight;
    }
}
