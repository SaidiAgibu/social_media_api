package com.saidi.social_media_plartform.exceptions;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ExceptionResponse {
    private HttpStatus status;
    private String message;
}
