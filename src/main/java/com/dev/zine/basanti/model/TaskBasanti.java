package com.dev.zine.basanti.model;

import com.dev.zine.model.User;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class TaskBasanti {
    private Date createdDate;
    private String title;
    private String subTitle;
    private String description;
    private Date dueDate;
    private String psLink;
//    private String submissionLink;
    private String type;
    private String recruitment;
//    private boolean visible;  //hide if not visible
    private List<InstanceBasanti> taskInatances;
    private List<User> mentors;
}
