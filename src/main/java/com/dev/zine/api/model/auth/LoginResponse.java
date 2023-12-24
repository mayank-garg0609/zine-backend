package com.dev.zine.api.model.auth;

import lombok.Data;

@Data
public class LoginResponse {

    private String jwt;

    private boolean success;

    private String failureReason;

}