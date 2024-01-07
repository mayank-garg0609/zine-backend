package com.dev.zine.api.controllers.payments;

import com.dev.zine.api.model.payments.OrderRequest;
import com.dev.zine.model.Payment;
import com.dev.zine.service.RazorpayService;
import com.razorpay.RazorpayException;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private RazorpayService razorpayService;

    @GetMapping("/create-order")
    public ResponseEntity createOrder(@RequestBody OrderRequest orderRequest) throws RazorpayException {
        String orderId = razorpayService.createOrder(orderRequest);
        return ResponseEntity.ok(orderId);
    }

    @PostMapping("/verify-payment")
    public String verifyPayment(@RequestParam String orderId, @RequestParam String paymentId, @RequestParam String signature) throws RazorpayException {
        boolean isValid = razorpayService.verifyPaymentSignature(orderId, paymentId, signature);
        return isValid ? "Payment Successful" : "Payment Failed";
    }
}
