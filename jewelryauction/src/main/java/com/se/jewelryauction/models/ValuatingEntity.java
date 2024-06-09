package com.se.jewelryauction.models;

import com.se.jewelryauction.models.enums.PaymentMethod;
import com.se.jewelryauction.models.enums.ValuatingMethod;
import com.se.jewelryauction.models.enums.ValuatingStatus;
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
public class ValuatingEntity  extends BaseEntiy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "staff_id")
    private UserEntity staff;

    @ManyToOne
    @JoinColumn(name = "jewelry_id")
    private JewelryEntity jewelry;

    private float valuation_value;
    private String notes;

    private ValuatingStatus status;

    private boolean isOnline;

    private float desiredPrice;
    private PaymentMethod paymentMethod;
    private float valuatingFee;
    private ValuatingMethod valuatingMethod;
    private String address;

}
