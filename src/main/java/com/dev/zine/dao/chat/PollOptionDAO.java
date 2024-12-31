package com.dev.zine.dao.chat;

import java.util.List;

import org.springframework.data.repository.ListCrudRepository;

import com.dev.zine.model.chat.Poll;
import com.dev.zine.model.chat.PollOption;

public interface PollOptionDAO extends ListCrudRepository<PollOption, Long>{
    List<PollOption> findAllByPoll(Poll poll);
}

