package com.dev.zine.service;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dev.zine.api.model.form.creation.FormCreateBody;
import com.dev.zine.dao.EventDAO;
import com.dev.zine.dao.chat.MessageDAO;
import com.dev.zine.dao.chat.PollDAO;
import com.dev.zine.dao.chat.PollOptionDAO;
import com.dev.zine.dao.form.FormDAO;
import com.dev.zine.dao.form.QuestionDAO;
import com.dev.zine.dao.form.ResponseDAO;
import com.dev.zine.exceptions.EventNotFound;
import com.dev.zine.model.Event;
import com.dev.zine.model.chat.Poll;
import com.dev.zine.model.chat.PollOption;
import com.dev.zine.model.chat.TextMessage;
import com.dev.zine.model.form.Form;
import com.dev.zine.model.form.Question;

@Service
public class FormService {
    @Autowired
    private FormDAO formDAO;
    @Autowired
    private QuestionDAO questionDAO;
    @Autowired
    private ResponseDAO responseDAO;
    @Autowired
    private MessageDAO messageDAO;
    @Autowired
    private PollDAO pollDAO;
    @Autowired
    private PollOptionDAO pollOptionDAO;
    @Autowired
    private EventDAO eventDAO;

    public void createForm(FormCreateBody body) throws EventNotFound{
        Event event = eventDAO.findById(body.getEventId()).orElseThrow(() -> new EventNotFound(body.getEventId()));
        Form form = Form.builder()
                .name(body.getName())
                .description(body.getDescription())
                .date(Timestamp.valueOf(LocalDateTime.now()))
                .event(event)
                .build();

        body.getQuestions().forEach(input -> {
            Question.QuestionBuilder questionBuilder = Question.builder().form(form).type(input.getType());
            if (input.getType().equals("text")) {
                TextMessage textMessage = TextMessage.builder()
                        .content(input.getTextMsgBody().getContent())
                        .build();
                messageDAO.save(textMessage);
                questionBuilder.text(textMessage);
            } else if (input.getType().equals("poll")) {
                Poll poll = Poll.builder()
                        .title(input.getPollBody().getTitle())
                        .description(input.getPollBody().getDescription())
                        .build();
                pollDAO.save(poll);
                input.getPollBody().getPollOptions().forEach(option -> {
                    PollOption newOption = PollOption.builder()
                            .poll(poll)
                            .value(option)
                            .build();
                    pollOptionDAO.save(newOption);
                });
                questionBuilder.poll(poll);
            }
            form.getQuestions().add(questionBuilder.build());
        });
        formDAO.save(form);
    }

    public List<Form> getAllForms() {
        return formDAO.findAll();
    }
}
