package com.dev.zine.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

import com.dev.zine.model.Message;
import com.dev.zine.model.Rooms;

import java.sql.Timestamp;

import java.util.Optional;
import java.util.List;

public interface MessagesDAO extends ListCrudRepository<Message, Long> {

    Optional<Message> findById(Long id);

    List<Message> findByRoomIdOrderByTimestampDesc(Rooms roomId);

    List<Message> findByRoomIdOrderByTimestampAsc(Rooms roomId);

    Optional<Message> findFirstByRoomIdOrderByTimestampDesc(Rooms roomId);

    @Query("SELECT COUNT(m) FROM Message m WHERE m.roomId = :room AND m.timestamp > :lastSeen")
    Long countUnreadMessages(@Param("room") Rooms room, @Param("lastSeen") Timestamp lastSeen);

    List<Message> findByRoomIdOrderByTimestampDesc(Rooms room, Pageable pageable);
}
