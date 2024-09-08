package com.dev.zine.api.model.room;

import java.sql.Timestamp;

import com.dev.zine.model.Rooms;

import lombok.Data;

@Data
public class RoomResBody {
    private Rooms room;
    private Timestamp lastMessageTimestamp;
    private Long unreadMessages;
    private Timestamp userLastSeen;
}
