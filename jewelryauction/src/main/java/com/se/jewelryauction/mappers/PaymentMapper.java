package com.se.jewelryauction.mappers;

import com.se.jewelryauction.models.Payment;
import com.se.jewelryauction.requests.PaymentRefundRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PaymentMapper {
    PaymentMapper INSTANCE = Mappers.getMapper(PaymentMapper.class);
    Payment toModel(PaymentRefundRequest request);
}
