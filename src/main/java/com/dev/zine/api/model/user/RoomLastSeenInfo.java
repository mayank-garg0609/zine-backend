package com.dev.zine.api.model.user;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class RoomLastSeenInfo {
    private Timestamp lastMessageTimestamp;
    private Long unreadMessages;
    private Timestamp userLastSeen;
}
