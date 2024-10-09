package com.dev.zine.exceptions;

public class UserNotInRoom extends Exception{
    public UserNotInRoom(Long user_id, Long room_id) {
        super("User " + user_id +  " does not belong to room " + room_id + ".");
    }
}
