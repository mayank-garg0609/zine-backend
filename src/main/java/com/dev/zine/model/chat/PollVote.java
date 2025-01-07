package com.dev.zine.model.chat;

import com.dev.zine.model.User;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "poll_votes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PollVote {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "voter", nullable = false)
    private User voter;

    @ManyToOne
    @JoinColumn(name = "poll", nullable = false)
    private Poll poll;

    @ManyToOne
    @JoinColumn(name = "poll_option", nullable = false)
    private PollOption option;
}
