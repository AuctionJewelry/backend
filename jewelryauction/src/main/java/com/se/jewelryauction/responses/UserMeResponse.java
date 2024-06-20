package com.se.jewelryauction.responses;

import com.se.jewelryauction.models.UserEntity;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserMeResponse {
    private UserEntity user;

    private float money;
}
