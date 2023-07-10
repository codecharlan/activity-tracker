package com.charlancodes.acttrack.dtos.requestDto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class UserDto {
    @NotBlank(message = "Please input your name")
    private String name;
    @Email(message = "Input email format")
    @NotBlank(message = "Please input your email")
    private String email;
    @NotBlank(message = "Please input your password")
    private String password;
}
