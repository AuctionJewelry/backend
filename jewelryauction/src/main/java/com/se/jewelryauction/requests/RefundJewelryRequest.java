package com.se.jewelryauction.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RefundJewelryRequest {

    private long jewelryId;

    private String full_name;

    private String phone_number;

    private String address;
}
