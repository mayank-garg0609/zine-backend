package com.dev.zine.basanti.model;

import lombok.Data;

import java.util.List;

@Data
public class InstanceBasanti {
//    room id
    private String type;
    private String name;
    private String status;
    private Integer completionPercentage;
    private List<String> comments;
    private List<CheckpointBasanti> checkpoints;
    private List<LinkBasanti> links;
}
