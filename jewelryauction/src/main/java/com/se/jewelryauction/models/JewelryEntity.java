package com.se.jewelryauction.models;

import com.se.jewelryauction.models.enums.JewelryCondition;
import com.se.jewelryauction.models.enums.JewelryStatus;
import com.se.jewelryauction.models.enums.Sex;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private UserEntity sellerId;

    private String name;

    @Column(columnDefinition = "LONGTEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    private float weight;

    private String size;

    private String color;

    @Enumerated(EnumType.STRING)
    private Sex sex;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private BrandEntity brand;

    @Enumerated(EnumType.STRING)
    private JewelryCondition jewelryCondition;

    private float staringPrice;

    @Enumerated(EnumType.STRING)
    private JewelryStatus status;

    @ManyToOne
    @JoinColumn(name = "collection_id")
    private CollectionEntity collection;

    @OneToMany(mappedBy = "jewelry", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<JewelryImageEntity> jewelryImages;

    @OneToMany(mappedBy = "jewelry", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<JewelryMaterialEntity> jewelryMaterials;

    @Column(name = "thumbnail", length = 300)
    private String thumbnail;

}
