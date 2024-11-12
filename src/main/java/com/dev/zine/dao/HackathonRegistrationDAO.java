package com.dev.zine.dao;

import org.springframework.data.repository.ListCrudRepository;

import com.dev.zine.model.HackathonRegistrations;
import com.dev.zine.model.User;

public interface HackathonRegistrationDAO extends ListCrudRepository< HackathonRegistrations, Long> {
    boolean existsByUserId(User user);
}
