package com.dev.zine.dao.chat;

import org.springframework.data.repository.ListCrudRepository;

import com.dev.zine.model.chat.PollOption;

public interface PollOptionDAO extends ListCrudRepository<PollOption, Long>{
    
}

