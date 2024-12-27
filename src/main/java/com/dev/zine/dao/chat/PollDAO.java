package com.dev.zine.dao.chat;

import org.springframework.data.repository.ListCrudRepository;

import com.dev.zine.model.chat.Poll;

public interface PollDAO extends ListCrudRepository<Poll, Long>{
    
}

