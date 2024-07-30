package com.dev.zine.api.model.userToRoles;

import com.dev.zine.api.model.role.Role;
import com.dev.zine.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class UserToRoles {

    private User user_id;
    private Role role_id;
}
