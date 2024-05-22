package com.se.jewelryauction.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "jewelry_images")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JewelryImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "jewelry_id")
    private JewelryEntity jewelry;

    private String url;
}
