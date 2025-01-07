package com.dev.zine.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dev.zine.api.model.form.creation.FormCreateBody;
import com.dev.zine.api.model.form.form_response.FormResponseBody;
import com.dev.zine.dao.EventDAO;
import com.dev.zine.dao.chat.MessageDAO;
import com.dev.zine.dao.chat.PollDAO;
import com.dev.zine.dao.chat.PollOptionDAO;
import com.dev.zine.dao.chat.PollVoteDAO;
import com.dev.zine.dao.form.FormDAO;
import com.dev.zine.dao.form.QuestionDAO;
import com.dev.zine.dao.form.ResponseDAO;
import com.dev.zine.exceptions.EventNotFound;
import com.dev.zine.exceptions.NotFoundException;
import com.dev.zine.model.Event;
import com.dev.zine.model.User;
import com.dev.zine.model.chat.Poll;
import com.dev.zine.model.chat.PollOption;
import com.dev.zine.model.chat.PollVote;
import com.dev.zine.model.chat.TextMessage;
import com.dev.zine.model.form.Form;
import com.dev.zine.model.form.Question;
import com.dev.zine.model.form.Response;

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
    private PollVoteDAO pollVoteDAO;
    @Autowired
    private EventDAO eventDAO;

    public void createForm(FormCreateBody body) throws EventNotFound {
        Event event = eventDAO.findById(body.getEventId()).orElseThrow(() -> new EventNotFound(body.getEventId()));
        Form form = Form.builder()
                .name(body.getName())
                .description(body.getDescription())
                .date(Timestamp.valueOf(LocalDateTime.now()))
                .event(event)
                .build();

        body.getQuestions().forEach(input -> {
            Question.QuestionBuilder questionBuilder = Question.builder().form(form).type(input.getType())
                    .required(input.isRequired());
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

    public void deleteForm(Long id) {
        formDAO.deleteById(id);
    }

    public void addResponse(Long id, List<FormResponseBody> responses, User user) throws NotFoundException {
        responses.forEach(input -> {
            try {
                Question question = questionDAO.findById(input.getQuestionId())
                        .orElseThrow(() -> new NotFoundException("Question", input.getQuestionId()));

                Response.ResponseBuilder responseBuilder = Response.builder()
                        .question(question)
                        .user(user);

                if (question.getType().equals("text")) {
                    TextMessage textMessage = TextMessage.builder()
                            .content(input.getTextResponse().getContent())
                            .build();
                    messageDAO.save(textMessage);
                    responseBuilder.textResponse(textMessage);
                } else if (question.getType().equals("poll")) {
                    PollOption pollOption = pollOptionDAO.findById(input.getPollResponse().getOptionId())
                            .orElseThrow(
                                    () -> new NotFoundException("PollOption", input.getPollResponse().getOptionId()));
                    PollVote pollVote = PollVote.builder()
                            .poll(question.getPoll())
                            .voter(user)
                            .option(pollOption)
                            .build();
                    pollVoteDAO.save(pollVote);
                    responseBuilder.pollResponse(pollVote);
                }
                responseDAO.save(responseBuilder.build());
            } catch (NotFoundException e) {
                throw new RuntimeException("Error processing response: " + e.getMessage(), e);
            }
        });

    }
}
