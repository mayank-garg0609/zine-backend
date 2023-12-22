package com.dev.zine.api.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * The body for the login requests.
 */
public class LoginBody {

    /** The username to log in with. */
    @NotNull
    @NotBlank
    private String email;
    /** The password to log in with. */
    @NotNull
    @NotBlank
    private String password;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
