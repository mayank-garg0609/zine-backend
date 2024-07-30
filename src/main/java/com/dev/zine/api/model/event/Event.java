package com.dev.zine.api.model.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Event {
    private Long id;
    private String description;
    private String type;
    private String name;
    private String venue;
//    private Recruitment recruitment;
    private Date start_date_time;
    private Date end_date_time;
}
