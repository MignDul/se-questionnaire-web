package com.example.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalDefaultExceptionHandler {

    // Convert a predefined exception to an HTTP Status code
    @ResponseStatus(value= HttpStatus.NOT_FOUND, reason="Id must be an integer.") // Default: 400
    @ExceptionHandler(NumberFormatException.class)
    public void idParseError() {
        // Nothing to do
    }
}
