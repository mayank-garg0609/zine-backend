package com.dev.zine.exceptions;

public class CheckpointNotFound extends Exception {
    public CheckpointNotFound(Long id) {
        super("Checkpoint " + id.toString() + " does not exist");
    }
}
