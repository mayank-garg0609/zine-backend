package com.dev.zine.dao;

import java.util.Optional;

import org.springframework.data.repository.ListCrudRepository;

import com.dev.zine.model.User;

public interface UserDAO extends ListCrudRepository<User, Long> {

    Optional<User> findByEmailIgnoreCase(String email);

}