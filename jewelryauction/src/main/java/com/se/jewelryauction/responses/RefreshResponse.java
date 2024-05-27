package com.se.jewelryauction.responses;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefreshResponse {
    private String accessToken;
}
