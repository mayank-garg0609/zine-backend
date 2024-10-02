package com.dev.zine.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import com.dev.zine.model.User;

public interface UserDAO extends ListCrudRepository<User, Long> {
    Optional<User> findByEmailIgnoreCase(String email);

    List<User> findByEmailIn(List<String> userEmails);

    @Query("SELECT u FROM User u WHERE u.email LIKE :emailPattern")
    List<User> findByEmailMatches(String emailPattern);
}