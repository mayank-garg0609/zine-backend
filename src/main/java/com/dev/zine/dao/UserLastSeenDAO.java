package com.dev.zine.dao;

import java.util.Optional;
import org.springframework.data.repository.ListCrudRepository;

import com.dev.zine.model.Rooms;
import com.dev.zine.model.User;
import com.dev.zine.model.UserLastSeen;

public interface UserLastSeenDAO extends ListCrudRepository<UserLastSeen, Long> {
    Optional<UserLastSeen> findByUserAndRoom(User user, Rooms room);
}
