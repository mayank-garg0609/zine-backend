package com.dev.zine.service;


import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import com.dev.zine.api.model.payments.OrderRequest;
import com.dev.zine.dao.PaymentsDAO;
import com.dev.zine.model.Payment;
import com.razorpay.Order;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RazorpayService {

    @Value("${razorpay.keyId}")
    private String keyId;

    @Value("${razorpay.keySecret}")
    private String keySecret;

    @Autowired
    private PaymentsDAO paymentsDao;

    public String createOrder(OrderRequest orderRequest) throws RazorpayException {
        RazorpayClient razorpayClient = new RazorpayClient(keyId, keySecret);
    
        JSONObject order = new JSONObject();
        order.put("amount", orderRequest.getAmount());
        order.put("currency", "INR");
        order.put("receipt", "order_receipt_" + System.currentTimeMillis());

        Order createdOrder = razorpayClient.orders.create(order);

        Payment payment = new Payment();
        payment.setOrder(createdOrder.get("id"));
        payment.setAmount(orderRequest.getAmount());
        payment.setEmail(orderRequest.getEmail());
        payment.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
        payment.setStatus("Due");

        paymentsDao.save(payment);

        return createdOrder.get("id");
    }

    public boolean verifyPaymentSignature(String orderId, String paymentId, String signature) throws RazorpayException {
        JSONObject options = new JSONObject();
        options.put("razorpay_order_id", orderId);
        options.put("razorpay_payment_id", paymentId);
        options.put("razorpay_signature", signature);

        return Utils.verifyPaymentSignature(options, keySecret);
    }

    // Additional methods for handling payment verification, refund, etc.
}
