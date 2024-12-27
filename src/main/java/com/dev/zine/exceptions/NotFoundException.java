package com.dev.zine.exceptions;

public class NotFoundException extends Exception {
    public NotFoundException(String className, Long id) {
        super(className + " " + id.toString() + " not found.");
    }
}