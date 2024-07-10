package com.se.jewelryauction.controllers;

import com.se.jewelryauction.components.apis.CoreApiResponse;
import com.se.jewelryauction.components.configurations.PaymentConfig;
import com.se.jewelryauction.mappers.PaymentMapper;
import com.se.jewelryauction.models.Payment;
import com.se.jewelryauction.models.ValuatingEntity;
import com.se.jewelryauction.models.enums.PaymentStatus;
import com.se.jewelryauction.repositories.IPaymentRepository;
import com.se.jewelryauction.requests.PaymentAmountRequest;
import com.se.jewelryauction.requests.PaymentRefundRequest;
import com.se.jewelryauction.responses.PaymentResponse;
import com.se.jewelryauction.responses.VNPAYResponse;
import com.se.jewelryauction.services.IPaymentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("${app.api.version.v1}/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final IPaymentService paymentService;
    private final IPaymentRepository paymentRepository;

    @GetMapping("create_payment")
    public CoreApiResponse<PaymentResponse> getAllValuating(
            @RequestParam float amount
    ) throws UnsupportedEncodingException {
        PaymentResponse paymentResponse = paymentService.createPayment(amount);
        return CoreApiResponse.success(paymentResponse);
    }

    @GetMapping("")
    public CoreApiResponse<List<Payment>> getAllPayments(){
        return CoreApiResponse.success(paymentService.getPayments());
    }

    @GetMapping("/vnpay/ipn")
    public ResponseEntity<VNPAYResponse> handleIPN(

            HttpServletRequest request
    ) {
        VNPAYResponse vnp = new VNPAYResponse();
        try {
            Map fields = new HashMap();
            for (Enumeration params = request.getParameterNames(); params.hasMoreElements(); ) {
                String fieldName = (String) params.nextElement();
                String fieldValue = request.getParameter(fieldName);
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    fields.put(fieldName, fieldValue);
                }
            }

            String vnp_SecureHash = request.getParameter("vnp_SecureHash");
            if (fields.containsKey("vnp_SecureHashType")) {
                fields.remove("vnp_SecureHashType");
            }
            if (fields.containsKey("vnp_SecureHash")) {
                fields.remove("vnp_SecureHash");
            }

            // Check checksum
            String signValue = PaymentConfig.hashAllFields(fields);
            if (signValue.equals(vnp_SecureHash)) {
                String id = (String) fields.get("vnp_TxnRef");

                //Xử lí logic ở đây cập nhật vào database
                if ("00".equals(request.getParameter("vnp_ResponseCode"))) {
                    //Xử lí logic thành công
                    paymentService.setData(id, fields);
                } else {
                    paymentService.setCancel(id);
                    //Xử lí logic thất bại rồi lưu vào database
                }
                vnp.setMessage("Confirm Success");
                vnp.setRspCode("00");
            } else {
                vnp.setMessage("Invalid Checksum");
                vnp.setRspCode("97");
            }
        } catch (Exception e) {
            vnp.setMessage("Unknow error");
            vnp.setRspCode("99");
        }
        return ResponseEntity.ok(vnp);
    }

    @PostMapping("/refund")
    public CoreApiResponse<Payment> createPaymentRefund(
            @Valid @RequestBody PaymentRefundRequest paymentRefundRequest
    ){
        Payment payment = paymentService.createPaymentRefund(paymentRefundRequest);
        return CoreApiResponse.success(payment);
    }

    @PutMapping("/update-status")
    public CoreApiResponse<Payment> updatePaymentStatus(
            @RequestParam String paymentId,
            @RequestParam PaymentStatus paymentStatus
    ) {
        Payment updatedPayment = paymentService.UpdatePaymentStatus(paymentId, paymentStatus);
        return CoreApiResponse.success(updatedPayment);
    }
}
