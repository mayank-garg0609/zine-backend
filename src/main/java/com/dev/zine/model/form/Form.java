package com.dev.zine.model.form;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.dev.zine.model.Event;
import com.dev.zine.model.chat.ChatItem;

@Entity
@Table(name = "forms")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Form {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String description;
    private Timestamp date;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "event_id", referencedColumnName = "id")
    private Event event;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "chat_item_id", referencedColumnName = "id")
    private ChatItem chatItem;

    @OneToMany(mappedBy = "form", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Question> questions = new ArrayList<>();
}

