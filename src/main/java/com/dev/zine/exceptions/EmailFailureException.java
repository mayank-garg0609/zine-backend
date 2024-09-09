package com.dev.zine.exceptions;

public class EmailFailureException extends Exception {
    public EmailFailureException() {
        super("error-sending-email");
    }
}
