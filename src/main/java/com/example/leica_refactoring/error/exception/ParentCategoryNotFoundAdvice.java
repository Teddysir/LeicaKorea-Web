package com.example.leica_refactoring.error.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


@ControllerAdvice
public class ParentCategoryNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(ParentCategoryNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String ParentCategoryNotFoundAdvice(ParentCategoryNotFoundException ex) {
        return ex.getMessage();
    }
}
