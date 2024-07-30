package com.dev.zine.api.model.task;

import com.dev.zine.model.Rooms;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task {

    private Long id;
    private Date createdDate;
    private String title;
    private String subtitle;
    private String description;
    private Date dueDate;
    private String ps_link;
    private String submission_link;
    private String room_name;
    private boolean create_room;
    private String type;
    private String recruitment;
    private boolean visible;
    private Rooms roomId;
}
