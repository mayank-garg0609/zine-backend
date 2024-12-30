package com.dev.zine.dao.chat;

import org.springframework.data.repository.ListCrudRepository;

import com.dev.zine.model.chat.ChatItem;
import java.util.List;
import com.dev.zine.model.Rooms;


public interface ChatItemDAO extends ListCrudRepository<ChatItem, Long>{
    List<ChatItem> findByRoomIdOrderByTimestampAsc(Rooms roomId);
    
}
