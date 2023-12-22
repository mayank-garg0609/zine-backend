package com.dev.zine.api.model;

import lombok.Data;

@Data
public class LoginResponse {

    /** The JWT token to be used for authentication. */
    private String jwt;

    private boolean success;
    /** The reason for failure on login. */
    private String failureReason;

}