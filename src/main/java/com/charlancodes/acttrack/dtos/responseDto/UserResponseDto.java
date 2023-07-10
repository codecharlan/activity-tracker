package com.charlancodes.acttrack.dtos.responseDto;

import lombok.Data;

import java.util.List;
@Data
public class UserResponseDto {
    private Long id;
    private String name;
    private String email;
    private List<Long> taskIds;
}


