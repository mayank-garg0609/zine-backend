package com.dev.zine.service;


import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;

import jakarta.transaction.Transactional;

import com.dev.zine.api.model.payments.OrderRequest;
import com.dev.zine.dao.PaymentDAO;
import java.util.Optional;

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

    
    private PaymentDAO paymentsDao;

    public RazorpayService(PaymentDAO paymentsDAO)
    {
        this.paymentsDao=paymentsDAO;
    }

    public String createOrder(OrderRequest orderRequest) throws RazorpayException {
        RazorpayClient razorpayClient = new RazorpayClient(keyId, keySecret);
    
        JSONObject order = new JSONObject();
        order.put("amount", orderRequest.getAmount());
        order.put("currency", "INR");
        order.put("receipt", "order_receipt_" + System.currentTimeMillis());

        Order createdOrder = razorpayClient.orders.create(order);

        Payment payment = new Payment();
        payment.setOrderId(createdOrder.get("id"));
        payment.setAmount(orderRequest.getAmount());
        payment.setEmail(orderRequest.getEmail());
        payment.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
        payment.setRemarks(orderRequest.getRemark());
        payment.setStatus("Due");
        System.out.println(payment);

        paymentsDao.save(payment);

        return createdOrder.get("id");
    }

    @Transactional
    public boolean verifyPaymentSignature(String orderId, String paymentId, String signature) throws RazorpayException {
        JSONObject options = new JSONObject();
        options.put("razorpay_order_id", orderId);
        options.put("razorpay_payment_id", paymentId);
        options.put("razorpay_signature", signature);

        if( Utils.verifyPaymentSignature(options, keySecret))
        {   
         Payment pay= paymentsDao.findByOrderId(orderId).orElse(null);
        if(pay==null) return false;
        pay.setStatus("paid");
        pay.setPayId(paymentId);
        pay.setSignature(signature);
        return true;
        }
        else
        {
            return false;
        }
    }

    // Additional methods for handling payment verification, refund, etc.
}
