package com.dev.zine.api.model.user;

import lombok.Data;

@Data
public class TokenUpdateBody {
    private String token;
    private String userEmail;
}
