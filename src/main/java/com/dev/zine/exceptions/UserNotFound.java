package com.dev.zine.exceptions;

public class UserNotFound extends Exception {
    public UserNotFound(Long id) {
        super("User "+id.toString()+" does not exist.");
    }
    public UserNotFound() {
        super("User does not exist.");
    }
}
