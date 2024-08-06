package com.dev.zine.api.controllers.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProjection {
    public String email;
    public String name; 
}
