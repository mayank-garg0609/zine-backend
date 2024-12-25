package com.dev.zine.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dev.zine.api.model.comments.CommentCreateBody;
import com.dev.zine.api.model.comments.CommentResponse;
import com.dev.zine.dao.TaskInstanceCommentsDAO;
import com.dev.zine.dao.UserDAO;
import com.dev.zine.dao.TaskInstanceDAO;
import com.dev.zine.exceptions.CommentNotFound;
import com.dev.zine.exceptions.TaskInstanceNotFound;
import com.dev.zine.exceptions.UserNotFound;
import com.dev.zine.model.TaskInstance;
import com.dev.zine.model.TaskInstanceComments;
import com.dev.zine.model.User;

@Service
public class InstanceCommentsService {
    @Autowired
    private TaskInstanceCommentsDAO commentsDAO;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private TaskInstanceDAO taskInstanceDAO;

    public CommentResponse createComment(Long id, CommentCreateBody body) throws UserNotFound, TaskInstanceNotFound {
        User sender = userDAO.findById(body.getSenderId()).orElseThrow(() -> new UserNotFound(body.getSenderId()));
        TaskInstance instance = taskInstanceDAO.findById(id).orElseThrow(() -> new TaskInstanceNotFound(id));
        TaskInstanceComments newComment = new TaskInstanceComments();
        newComment.setMessage(body.getMessage());
        newComment.setTaskInstance(instance);
        newComment.setSender(sender);
        newComment.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
        commentsDAO.save(newComment);

        CommentResponse resBody = new CommentResponse();
        resBody.setCommentId(newComment.getCommentId());
        resBody.setMessage(newComment.getMessage());
        resBody.setSenderEmail(newComment.getSender().getEmail());
        resBody.setSenderId(newComment.getSender().getId());
        resBody.setSenderName(newComment.getSender().getName());
        resBody.setTaskId(newComment.getTaskInstance().getTaskId().getId());
        resBody.setTaskInstance(newComment.getTaskInstance().getTaskInstanceId());
        resBody.setTimestamp(newComment.getTimestamp());
        return resBody;
    }

    public CommentResponse updateComment(Long id, CommentCreateBody body) throws CommentNotFound {
        TaskInstanceComments existing = commentsDAO.findById(id).orElseThrow(() -> new CommentNotFound(id));
        existing.setMessage(body.getMessage());
        commentsDAO.save(existing);
        CommentResponse resBody = new CommentResponse();
        resBody.setCommentId(existing.getCommentId());
        resBody.setMessage(existing.getMessage());
        resBody.setSenderEmail(existing.getSender().getEmail());
        resBody.setSenderId(existing.getSender().getId());
        resBody.setSenderName(existing.getSender().getName());
        resBody.setTaskInstance(existing.getTaskInstance().getTaskInstanceId());
        resBody.setTimestamp(existing.getTimestamp());
        resBody.setTaskId(existing.getTaskInstance().getTaskId().getId());
        return resBody;
    }

    public List<CommentResponse> getInstanceComments(Long id) throws TaskInstanceNotFound{
        TaskInstance instance = taskInstanceDAO.findById(id).orElseThrow(() -> new TaskInstanceNotFound(id));
        List<TaskInstanceComments> comments = commentsDAO.findByTaskInstance(instance);
        return comments.stream().map(comment -> {
            CommentResponse resBody = new CommentResponse();
            resBody.setCommentId(comment.getCommentId());
            resBody.setMessage(comment.getMessage());
            resBody.setSenderEmail(comment.getSender().getEmail());
            resBody.setSenderId(comment.getSender().getId());
            resBody.setSenderName(comment.getSender().getName());
            resBody.setTaskInstance(comment.getTaskInstance().getTaskInstanceId());
            resBody.setTimestamp(comment.getTimestamp());
            resBody.setTaskId(comment.getTaskInstance().getTaskId().getId());
            return resBody;
        }).collect(Collectors.toList());
    }

    public void deleteComment(Long id) {
        commentsDAO.deleteById(id);
    }
}
