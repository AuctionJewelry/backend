package com.se.jewelryauction.responses;

import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListBidForAuction {
    private Long id;
    private Long auctionId;
    private String userName;
    private float bidAmount;
    private LocalDateTime bidTime;
    private String status;
}
