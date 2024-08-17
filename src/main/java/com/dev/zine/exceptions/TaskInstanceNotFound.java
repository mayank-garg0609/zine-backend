package com.dev.zine.exceptions;

public class TaskInstanceNotFound extends Exception {
    public TaskInstanceNotFound(Long id) {
        super("Task instance "+id.toString()+ " not found.");
      }
}
