package com.dev.zine.dao;

import org.springframework.data.repository.ListCrudRepository;

import com.dev.zine.model.Message;
import com.dev.zine.model.Rooms;

import java.util.Optional;
import java.util.List;

public interface MessagesDAO extends ListCrudRepository<Message, Long> {

    Optional<Message> findById(Long id);

    List<Message> findByRoomIdOrderByTimestampDesc(Rooms roomId);

    List<Message> findByRoomIdOrderByTimestampAsc(Rooms roomId);
}
