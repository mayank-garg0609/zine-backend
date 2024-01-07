package com.dev.zine.api.model.payments;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderRequest {
    @NotNull
    private Float amount;

    @NotNull
    private String email;

    private String remark;
}
