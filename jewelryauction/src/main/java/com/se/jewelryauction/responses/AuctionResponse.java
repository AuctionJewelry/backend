package com.se.jewelryauction.responses;

import com.se.jewelryauction.models.enums.AuctionStatus;
import lombok.*;

import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuctionResponse {
    private Long id;
    private String jewelryName;
    private Date startTime;
    private Date endTime;
    private float currentPrice;
    private AuctionStatus status;
}
