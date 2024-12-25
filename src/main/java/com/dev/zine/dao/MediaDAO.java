package com.dev.zine.dao;

import java.util.Optional;

import org.springframework.data.repository.ListCrudRepository;

import com.dev.zine.model.Media;

public interface MediaDAO extends ListCrudRepository<Media, Long>{
    Optional<Media> findByUrl(String url);
}
