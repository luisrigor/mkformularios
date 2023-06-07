package com.gsc.mkformularios.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
public class ApiError {
    private Long id;
    private LocalDateTime timestamp;
    private HttpStatus status;
    private String message;
    private String details;

    public ApiError(Long id, HttpStatus status, String message, String details) {
        this.id = id;
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.message = message;
        this.details = details;
    }
}
