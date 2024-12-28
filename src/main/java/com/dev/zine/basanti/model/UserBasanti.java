package com.dev.zine.basanti.model;

import lombok.Data;

import java.util.List;

@Data
public class UserBasanti {
    private long id;
    private String name;
    private String email;
    private String type;
    private boolean registered;
    private boolean emailVerified;
    List<String> roleName;
}
