package com.se.jewelryauction.models;

import com.se.jewelryauction.models.Enum.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "orders")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private UserEntity buyer;

    private String fullName;
    @ManyToOne
    @JoinColumn(name = "delivery_id")
    private DeliveryMethodEntity deliveryMethod;

    private String note;
    private Date orderDate;
    private OrderStatus status;
    private Date shippingDate;
    private String paymentMethod;
    private float total_money;
}
