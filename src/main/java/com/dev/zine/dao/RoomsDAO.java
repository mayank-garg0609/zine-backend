package com.dev.zine.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

import com.dev.zine.model.Rooms;
import java.util.List;

public interface RoomsDAO extends ListCrudRepository<Rooms, Long> {

    Optional<Rooms> findById(Long id);

    Optional<Rooms> findByName(String name);

    List<Rooms> findByType(String type);

    @Query("SELECT r.id FROM Rooms r WHERE r.type = :type")
    List<Long> getRoomIdsByType(@Param("type") String type);

}