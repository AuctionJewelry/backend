package com.se.jewelryauction.models;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "auto_biddings")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AutoBiddingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "auction_id")
    private AuctionEntity auction;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private UserEntity customer;

    private float maxBid;

    private Date bidTime;
}
