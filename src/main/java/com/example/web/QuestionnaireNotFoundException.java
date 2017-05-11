package com.example.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such questionnaire.")
public class QuestionnaireNotFoundException extends RuntimeException {

    public QuestionnaireNotFoundException(Long id) {
        super("Questionnaire not found by id=" + id);
    }
}
