package com.dev.zine.exceptions;

public class TaskNotFoundException extends Exception {
    public TaskNotFoundException(){
        super("Task not found");
    }
}
