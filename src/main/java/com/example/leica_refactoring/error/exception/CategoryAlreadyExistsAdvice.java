package com.example.leica_refactoring.error.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class CategoryAlreadyExistsAdvice {

    @ResponseBody
    @ExceptionHandler(value = CategoryAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.ALREADY_REPORTED)
    String categoryAlreadyExistsHandler(CategoryAlreadyExistsException ex) {
        return ex.getMessage();
    }

}
