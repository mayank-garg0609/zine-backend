package com.dev.zine.model.form;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import com.dev.zine.model.chat.Poll;
import com.dev.zine.model.chat.TextMessage;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "questions")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Builder.Default
    private boolean required = true;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "form_id")
    private Form form;

    private String type;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "text_message_id", referencedColumnName = "id")
    private TextMessage text;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "poll_id", referencedColumnName = "id")
    private Poll poll;

    @JsonIgnore
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Response> responses;
}

