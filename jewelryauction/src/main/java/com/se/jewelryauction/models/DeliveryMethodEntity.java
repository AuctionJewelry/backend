package com.se.jewelryauction.models;

import com.se.jewelryauction.models.enums.DeliveryStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "delivery_method")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeliveryMethodEntity extends BaseEntiy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "jewelry_id")
    private JewelryEntity jewelry;

    @ManyToOne
    @JoinColumn(name = "staff_id")
    private UserEntity staff;

    private String full_name;

    private String phone_number;

    private String address;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;

    private boolean valuatingDelivery;

    private LocalDateTime receiving_time;

}
