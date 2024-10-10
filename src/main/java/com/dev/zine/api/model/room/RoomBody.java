package com.dev.zine.api.model.room;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RoomBody {

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @NotBlank
    private String type;

    private String description;
    private String dpUrl;
    private String imagePath;
}
