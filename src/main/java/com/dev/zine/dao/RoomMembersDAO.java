package com.dev.zine.dao;

import org.springframework.data.repository.ListCrudRepository;


import com.dev.zine.model.RoomMembers;
import com.dev.zine.model.Rooms;
import com.dev.zine.model.User;

import java.util.List;

public interface RoomMembersDAO extends ListCrudRepository<RoomMembers, Long> {

    List<RoomMembers> findByRoom(Rooms room);

    List<RoomMembers> findByUser(User user);

    boolean existsByUserAndRoom(User user, Rooms room);

    void deleteByUserAndRoom(User user, Rooms room);

}
