package com.dev.zine.exceptions;

public class StageAlreadyExists extends Exception {
    public StageAlreadyExists(Long id) {
        super("Stage "+id.toString()+" already exists.");
    }
}
