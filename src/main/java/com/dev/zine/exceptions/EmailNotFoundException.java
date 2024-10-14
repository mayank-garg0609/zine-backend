package com.dev.zine.exceptions;

public class EmailNotFoundException extends Exception {
    public EmailNotFoundException() {
        super("Email does not exist.");
    }
}
