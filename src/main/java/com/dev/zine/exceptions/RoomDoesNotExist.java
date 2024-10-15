package com.dev.zine.exceptions;

public class RoomDoesNotExist extends Exception {
    public RoomDoesNotExist() {
        super("Room does not exist");
    }
}
