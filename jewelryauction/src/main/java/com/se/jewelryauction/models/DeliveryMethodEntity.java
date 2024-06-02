package com.se.jewelryauction.models;

import com.se.jewelryauction.models.enums.DeliveryStatus;
import jakarta.persistence.*;
import lombok.*;

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
    @JoinColumn(name = "staff_id")
    private UserEntity staff;
    private String full_name;
    private String phone_number;
    private String address;
    private DeliveryStatus status;

}
