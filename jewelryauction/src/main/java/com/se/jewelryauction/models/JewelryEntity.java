package com.se.jewelryauction.models;

import com.se.jewelryauction.models.enums.JewelryCondition;
import com.se.jewelryauction.models.enums.JewelryStatus;
import com.se.jewelryauction.models.enums.Sex;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "jewelrys")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JewelryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "seller_id")
    private UserEntity sellerId;

    private String name;

    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    private float weight;

    private String size;

    private String color;

    private Sex sex;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private BrandEntity brand;

    private JewelryCondition jewelryCondition;
    private float staringPrice;
    private JewelryStatus status;

    @ManyToOne
    @JoinColumn(name = "collection_id")
    private CollectionEntity collection;
}
