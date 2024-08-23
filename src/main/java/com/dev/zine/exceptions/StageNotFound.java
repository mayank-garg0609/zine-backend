package com.dev.zine.exceptions;

public class StageNotFound extends Exception {
    public StageNotFound(Long id) {
        super("Stage "+id.toString()+" does not exist.");
    }
}
