package com.dev.zine.exceptions;

public class CommentNotFound extends Exception {
    public CommentNotFound(Long id) {
        super("Comment "+id.toString()+" does not exist.");
    }
}

