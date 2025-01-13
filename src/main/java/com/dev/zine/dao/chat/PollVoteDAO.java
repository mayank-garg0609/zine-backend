package com.dev.zine.dao.chat;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

import com.dev.zine.model.User;
import com.dev.zine.model.chat.Poll;
import com.dev.zine.model.chat.PollOption;
import com.dev.zine.model.chat.PollVote;

public interface PollVoteDAO extends ListCrudRepository<PollVote, Long> {
    boolean existsByVoterAndPoll(User voter, Poll poll);
    void deleteAllByVoterAndPoll(User voter, Poll poll);
    int countByOption(PollOption option);
    Optional<PollVote> findByVoterAndPoll(User voter, Poll poll);
    @Query("SELECT v.voter.id from PollVote v WHERE v.option = :option")
    List<Long> getVoterIds(PollOption option);
}
