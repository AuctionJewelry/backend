package com.se.jewelryauction.services.implement;

import com.se.jewelryauction.components.configurations.PaymentConfig;
import com.se.jewelryauction.components.exceptions.AppException;
import com.se.jewelryauction.components.securities.UserPrincipal;
import com.se.jewelryauction.models.Payment;
import com.se.jewelryauction.models.UserEntity;
import com.se.jewelryauction.models.WalletEntity;
import com.se.jewelryauction.models.enums.PaymentForType;
import com.se.jewelryauction.models.enums.PaymentStatus;
import com.se.jewelryauction.repositories.IPaymentRepository;
import com.se.jewelryauction.repositories.IWalletRepository;
import com.se.jewelryauction.responses.PaymentResponse;
import com.se.jewelryauction.services.IPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PaymentService implements IPaymentService {
    public final IWalletRepository walletRepository;
    public final IPaymentRepository paymentRepositorty;

    @Override
    public PaymentResponse createPayment(float total) throws UnsupportedEncodingException {
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String vnp_TxnRef = "";
        Payment payment1 = new Payment();
        WalletEntity wallet = walletRepository.findByUser(this.getCurrentUser());
        if(wallet == null){
            wallet = new WalletEntity();
            wallet.setUser(this.getCurrentUser());
            wallet.setMoney(0);
            wallet = walletRepository.save(wallet);
        }
        vnp_TxnRef = "W" + new DecimalFormat("#00000").format(wallet.getId());


        String orderType = "other";
        String totalString = total + "";
        int index = totalString.indexOf(".");
        totalString = totalString.substring(0, index);
        Long amount = Long.parseLong(totalString) * 100;

//        DecimalFormat df = new DecimalFormat("#000000");
//        String vnp_TxnRef = df.format(id);

        String vnp_IpAddr = "14.191.95.88";
        String vnp_TmnCode = PaymentConfig.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");

        vnp_Params.put("vnp_OrderInfo", PaymentForType.DEPOSIT.toString());
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", PaymentConfig.vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        vnp_Params.put("vnp_BankCode", "NCB");
        vnp_Params.put("vnp_OrderType", orderType);

        TimeZone vietnamTimeZone = TimeZone.getTimeZone("Asia/Ho_Chi_Minh");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        sdf.setTimeZone(vietnamTimeZone);

        Date currentDate = new Date();
        String vnp_CreateDate = sdf.format(currentDate);
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        vnp_TxnRef += vnp_CreateDate;
        payment1 = new Payment(LocalDateTime.now(), LocalDateTime.now(), vnp_TxnRef, wallet, amount/100f, PaymentForType.DEPOSIT, "NCB", PaymentStatus.PENDING);
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);

        Instant instant = currentDate.toInstant();
        Instant newInstant = instant.plus(Duration.ofDays(1));
        Date newDate = Date.from(newInstant);
        String vnp_ExpireDate = sdf.format(newDate.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = PaymentConfig.hmacSHA512(PaymentConfig.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;//
        String paymentUrl = PaymentConfig.vnp_PayUrl + "?" + queryUrl;


        Payment currPayment =  paymentRepositorty.findPaymentById(vnp_TxnRef);
        if(currPayment != null){
            if(currPayment.getStatus() == PaymentStatus.SUCCESS){
                return new PaymentResponse("Failed", "This payment is paid before!", "", currPayment);
            }
            else{
                currPayment.setUpdatedAt(LocalDateTime.now());
                paymentRepositorty.save(currPayment);
            }
        }
        else{
            paymentRepositorty.save(payment1);
        }

        PaymentResponse paymentRespone = new PaymentResponse("OK", "Successfully", paymentUrl, payment1);
//        this.sendRedirect(paymentUrl);
        return paymentRespone;
    }

    @Override
    public Payment setData(String id, Map field) {
        Payment existingPayment = paymentRepositorty.findPaymentById(id);
        if(existingPayment != null){
            WalletEntity wallet = existingPayment.getWallet();
            if(wallet != null){
                wallet.setMoney(wallet.getMoney() + existingPayment.getAmount());
                walletRepository.save(wallet);

                existingPayment.setStatus(PaymentStatus.SUCCESS);
                String bankCode = (String) field.get("vnp_BankCode");
                String bankTranNo = (String) field.get("vnp_BankTranNo");
                String cardType = (String) field.get("vnp_CardType");
                String payDate = (String) field.get("vnp_PayDate");
                String tranNo = (String) field.get("vnp_TransactionNo");
                String transactionStatus = (String) field.get("vnp_TransactionStatus");
                existingPayment.setUpdatedAt(LocalDateTime.now());
                existingPayment.setBankCode(bankCode);
                existingPayment.setBankTranNo(bankTranNo);
                existingPayment.setCardType(cardType);
                existingPayment.setPayDate(payDate);
                existingPayment.setTransactionNo(tranNo);
                existingPayment.setTransactionStatus(transactionStatus);

                existingPayment = paymentRepositorty.save(existingPayment);
            }
            else{
                throw new AppException(HttpStatus.BAD_REQUEST, "There are no wallet!");
            }
        }
        else{
            throw new AppException(HttpStatus.BAD_REQUEST, "There are no transaction!");
        }
        return  existingPayment;
    }

    @Override
    public Payment setCancel(String id) {
        Payment existingPayment = paymentRepositorty.findPaymentById(id);
        if(existingPayment != null){
            existingPayment.setStatus(PaymentStatus.FAIL);
        }
        else{
            throw new AppException(HttpStatus.BAD_REQUEST, "There are no transaction!");
        }
        return  existingPayment;
    }

    private UserEntity getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UserEntity user = userPrincipal.getUser();
        return user;
    }
}
