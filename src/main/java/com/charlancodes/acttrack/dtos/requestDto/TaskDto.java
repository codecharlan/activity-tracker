package com.charlancodes.acttrack.dtos.requestDto;

import com.charlancodes.acttrack.entities.TaskStatus;
import com.charlancodes.acttrack.entities.User;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;

@Data
@RequiredArgsConstructor
public class TaskDto {
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    private TaskStatus status;
    @ManyToOne
    private User user;
}
