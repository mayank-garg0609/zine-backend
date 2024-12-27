package com.dev.zine.dao.chat;

import org.springframework.data.repository.ListCrudRepository;

import com.dev.zine.model.chat.TextMessage;

public interface MessageDAO extends ListCrudRepository<TextMessage, Long>{
    
}
