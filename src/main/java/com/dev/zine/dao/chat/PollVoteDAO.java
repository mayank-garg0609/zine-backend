package com.dev.zine.dao.chat;

import org.springframework.data.repository.ListCrudRepository;

import com.dev.zine.model.chat.PollVote;

public interface PollVoteDAO extends ListCrudRepository<PollVote, Long>{
    
}

