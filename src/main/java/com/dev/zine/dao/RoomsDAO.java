package com.dev.zine.dao;

import java.util.Optional;

import org.springframework.data.repository.ListCrudRepository;

import com.dev.zine.model.Rooms;
import java.util.List;

public interface RoomsDAO extends ListCrudRepository<Rooms, Long> {

    Optional<Rooms> findById(Long id);

    Optional<Rooms> findByName(String name);

    List<Rooms> findByType(String type);

}