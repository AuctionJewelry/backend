package com.se.jewelryauction.responses;

import com.se.jewelryauction.models.JewelryEntity;
import com.se.jewelryauction.models.MaterialEntity;
import com.se.jewelryauction.models.UserEntity;
import com.se.jewelryauction.models.ValuatingEntity;
import com.se.jewelryauction.models.enums.PaymentMethod;
import com.se.jewelryauction.models.enums.ValuatingMethod;
import com.se.jewelryauction.models.enums.ValuatingStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ValuatingResponse{
    private Long id;
    private UserEntity staff;
    private JewelryEntity jewelry;
    private float valuation_value;
    private String notes;
    private ValuatingStatus status;
    private boolean isOnline;
    private float desiredPrice;
    private PaymentMethod paymentMethod;
    private float valuatingFee;
    private ValuatingMethod valuatingMethod;
    private String address;
    public List<ValuatingPerMaterialResponse> materialPriceResponse;
    public PaymentResponse paymentResponse;
}