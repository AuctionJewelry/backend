package com.se.jewelryauction.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "jewelry_materials")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JewelryMaterialEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "jewelry_id")
    private JewelryEntity jewelry;

    @ManyToOne
    @JoinColumn(name = "material_id")
    private MaterialEntity material;
}
