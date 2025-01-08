package com.dev.zine.exceptions;

public class FormIsClosed extends Exception{
    public FormIsClosed() {
        super("The form has been closed.");
    }
}
