package com.dev.zine.exceptions;

public class TaskNotFoundException extends Exception {
    public TaskNotFoundException(Long id){
        super("Task "+id.toString()+" not found");
    }
}
