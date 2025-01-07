package com.dev.zine.model.form;

import com.dev.zine.model.User;
import com.dev.zine.model.chat.PollVote;
import com.dev.zine.model.chat.TextMessage;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.auto.value.AutoValue.Builder;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "responses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Response {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "text_response_id")
    private TextMessage textResponse;

    @OneToOne
    @JoinColumn(name = "poll_reponse_id")
    private PollVote pollResponse;
}
