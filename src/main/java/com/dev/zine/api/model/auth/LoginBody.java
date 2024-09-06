package com.dev.zine.api.model.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * The body for the login requests.
 */
public class LoginBody {

    @NotNull
    @NotBlank
    private String email;

    @NotNull
    @NotBlank
    private String password;

    private String pushToken;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPushToken() {
        return pushToken;
    }
}
