package com.dev.zine.exceptions;

public class EventNotFound extends Exception{
    public EventNotFound(Long id) {
        super("Event "+id.toString()+" does not exist.");
    }
}
