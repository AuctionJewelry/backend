package com.se.jewelryauction.models;

import com.se.jewelryauction.models.enums.AuctionStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "auctions")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuctionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "jewelry_id")
    private JewelryEntity jewelry;

    private Date startTime;
    private Date endTime;
    private float step;
    private int totalBids;
    private float currentPrice;
    private AuctionStatus status;
}
