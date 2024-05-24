package com.se.jewelryauction.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "materials")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MaterialEntity extends BaseEntiy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private BrandEntity brand_id;
}
