package com.se.jewelryauction.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuctionRequest {
    private Long jewelryId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date endTime;

    private float step;

}
