package com.dev.zine.api.controllers.payments;

import com.dev.zine.api.model.payments.OrderRequest;
import com.dev.zine.api.model.payments.VerifyOrderRequest;
import com.dev.zine.model.Payment;
import com.dev.zine.service.RazorpayService;
import com.razorpay.RazorpayException;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api")
public class PaymentController {

    @Autowired
    private RazorpayService razorpayService;

    @PostMapping("/create-order")
    public ResponseEntity createOrder(@RequestBody OrderRequest orderRequest) throws RazorpayException {
        Payment order = razorpayService.createOrder(orderRequest);
        return ResponseEntity.ok(order);
    }

    @PostMapping("/verify-payment")
    public Boolean verifyPayment(@RequestBody VerifyOrderRequest verifyRequest) throws RazorpayException {
        boolean isValid = razorpayService.verifyPaymentSignature(verifyRequest);
        System.out.println(isValid);
        return isValid;
    }

    @GetMapping("/get-donations")
    public ResponseEntity getDonations() {
        List<Payment> donations = razorpayService.getCompletedPayments();
        return ResponseEntity.ok(donations);
    }
    
}
