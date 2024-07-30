package com.dev.zine.model.UserToRole;

import com.dev.zine.model.Roles;
import com.dev.zine.model.User;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_task_assigned")
@Data
@AllArgsConstructor
@NoArgsConstructor
@IdClass(UserToRolesPKID.class)

public class UserToRoles {

//    remove it
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int id;

    @Id
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user_id;

    @Id
    @ManyToOne
    @JoinColumn(name ="role_id")
    private Roles role_id;
}
