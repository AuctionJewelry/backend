package com.se.jewelryauction.services;

import com.se.jewelryauction.models.Payment;
import com.se.jewelryauction.models.enums.PaymentStatus;
import com.se.jewelryauction.requests.PaymentRefundRequest;
import com.se.jewelryauction.responses.PaymentResponse;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

public interface IPaymentService {
//    public PaymentResponse createPayment(float total, PaymentForType payment, Long id) throws UnsupportedEncodingException;
    public List<Payment> getPayments();
    public PaymentResponse createPayment(float total) throws UnsupportedEncodingException;
//    public PaymentResponse createPaymentForValuating(float total, Long valutingId) throws UnsupportedEncodingException;
    public Payment createPaymentRefund(PaymentRefundRequest request);
    public Payment UpdatePaymentStatus(String id, PaymentStatus paymentStatus);
    public Payment setData(String id, Map field);

    Payment setCancel(String id);

    List<Payment> paymentMe();
}
