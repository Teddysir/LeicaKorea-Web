package com.example.leica_refactoring.error.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class AuthorOnlyAccessHandler {

    @ResponseBody
    @ExceptionHandler(AuthorOnlyAccessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String AuthorOnlyAccessHandler(AuthorOnlyAccessException ex) {
        return ex.getMessage();
    }
}
