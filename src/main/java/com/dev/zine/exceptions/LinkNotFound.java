package com.dev.zine.exceptions;

public class LinkNotFound extends Exception{
    public LinkNotFound(Long id) {
        super("Checkpoint " + id.toString() + " does not exist");
    }
}
