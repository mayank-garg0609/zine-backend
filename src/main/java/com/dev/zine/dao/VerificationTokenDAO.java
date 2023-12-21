package com.dev.zine.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.ListCrudRepository;

import com.dev.zine.model.User;
import com.dev.zine.model.VerificationToken;

public interface VerificationTokenDAO extends ListCrudRepository<VerificationToken, Long> {

    Optional<VerificationToken> findByToken(String token);

    void deleteByUser(User user);

    List<VerificationToken> findByUser_IdOrderByIdDesc(Long id);

}