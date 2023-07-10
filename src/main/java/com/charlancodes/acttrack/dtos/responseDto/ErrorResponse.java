package com.charlancodes.acttrack.dtos.responseDto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Data
@Getter
@Setter
public class ErrorResponse {

    private String message;
    private HttpStatus status;
    private String debugMessage;
}
