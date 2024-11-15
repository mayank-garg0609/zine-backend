package com.dev.zine.api.model.event;

import java.util.List;

import com.dev.zine.api.model.user.UserResponseBody;

import lombok.Data;

@Data
public class HackathonRegistrationsBody {
    private List<UserResponseBody> list;
    private Integer numRegistrations;
}
