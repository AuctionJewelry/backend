package com.se.jewelryauction.models;

import com.se.jewelryauction.models.Enum.ValuatingStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "valuating")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ValuatingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "staff_id")
    private UserEntity staff;

    @ManyToOne
    @JoinColumn(name = "jewelry_id")
    private JewelryEntity jewelry;

    private float desiredPrice;
    private String paymentMethod;
    private float valuation_value;
    private String notes;

    @ManyToOne
    @JoinColumn(name = "delivery_id")
    private DeliveryMethodEntity deliveryMethod;

    private ValuatingStatus status;


}
