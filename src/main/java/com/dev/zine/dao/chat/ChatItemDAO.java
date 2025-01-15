package com.dev.zine.dao.chat;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

import com.dev.zine.model.chat.ChatItem;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import com.dev.zine.model.Rooms;

public interface ChatItemDAO extends ListCrudRepository<ChatItem, Long> {
    List<ChatItem> findByRoomIdOrderByTimestampAsc(Rooms roomId);

    List<ChatItem> findByReplyTo(ChatItem replyTo);

    @Query("SELECT COUNT(m) FROM ChatItem m WHERE m.roomId = :room AND m.timestamp > :lastSeen AND m.deleted = false")
    Long countUnreadMessages(@Param("room") Rooms room, @Param("lastSeen") Timestamp lastSeen);

    Optional<ChatItem> findFirstByRoomIdAndDeletedFalseOrderByTimestampDesc(Rooms roomId);

}
