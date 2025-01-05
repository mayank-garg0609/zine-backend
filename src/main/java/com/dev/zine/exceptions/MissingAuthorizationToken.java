package com.dev.zine.exceptions;

public class MissingAuthorizationToken extends Exception {
    public MissingAuthorizationToken() {
        super("Authorization token is missing.");
    }
}
