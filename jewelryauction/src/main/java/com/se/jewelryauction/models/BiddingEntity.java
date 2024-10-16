package com.se.jewelryauction.models;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "biddings")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BiddingEntity extends BaseEntiy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "auction_id")
    private AuctionEntity auction;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private UserEntity customer;

    private float bidAmount;

    private LocalDateTime bidTime;


    private boolean autoBid;
}
