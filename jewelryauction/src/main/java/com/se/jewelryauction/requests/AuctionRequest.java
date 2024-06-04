package com.se.jewelryauction.requests;

import lombok.*;

import java.util.Date;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuctionRequest {
    private Long jewelryId;

    private Date startTime;

    private Date endTime;


}
