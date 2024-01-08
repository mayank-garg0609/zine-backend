package com.dev.zine.api.model.payments;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VerifyOrderRequest {
    @NotNull
    private String paymentId;

    @NotNull
    private String orderId;

    @NotNull
    private String signature;
}
