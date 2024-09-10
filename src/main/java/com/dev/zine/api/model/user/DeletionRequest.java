package com.dev.zine.api.model.user;

import lombok.Data;

@Data
public class DeletionRequest {
    private String email;
    private String password;
    private String deletionOption;
}