package com.se.jewelryauction.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VNPAYResponse {
    @JsonProperty("RspCode")
    private String RspCode;

    @JsonProperty("Message")
    private String Message;
}
