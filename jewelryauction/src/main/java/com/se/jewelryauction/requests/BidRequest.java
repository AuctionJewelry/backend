package com.se.jewelryauction.requests;

import lombok.*;


@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BidRequest {
    private long auctionId;

    private float bidAmount;

}
