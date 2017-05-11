package com.example.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such reply.")
public class ReplyNotFoundException extends RuntimeException {

    public ReplyNotFoundException(Long id) {
        super("Reply not found by id=" + id);
    }
}
