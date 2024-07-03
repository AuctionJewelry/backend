package com.se.jewelryauction.services;

import com.se.jewelryauction.models.Payment;
import com.se.jewelryauction.responses.PaymentResponse;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public interface IPaymentService {
//    public PaymentResponse createPayment(float total, PaymentForType payment, Long id) throws UnsupportedEncodingException;
    public PaymentResponse createPayment(float total) throws UnsupportedEncodingException;
    public PaymentResponse createPaymentForValuating(float total, Long valutingId) throws UnsupportedEncodingException;
    public Payment setData(String id, Map field);

    Payment setCancel(String id);
}
