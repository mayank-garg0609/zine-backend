package com.dev.zine.model.UserToRole;

import com.dev.zine.model.Roles;
import com.dev.zine.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class UserToRolesPKID implements java.io.Serializable {
    private User user_id;
    private Roles role_id;
}
