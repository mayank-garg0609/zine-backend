package com.dev.zine.exceptions;

public class IncorrectPasswordException extends Exception{
    public IncorrectPasswordException() {
        super( "wrong-password");
    }
}
