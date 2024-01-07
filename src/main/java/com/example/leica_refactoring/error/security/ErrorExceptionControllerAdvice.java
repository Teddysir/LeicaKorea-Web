package com.example.leica_refactoring.error.security;

import com.example.leica_refactoring.error.exception.requestError.BadRequestException;
import com.example.leica_refactoring.error.exception.requestError.ExpiredAccessTokenException;
import com.example.leica_refactoring.error.exception.requestError.ExpiredRefreshTokenException;
import com.example.leica_refactoring.error.exception.requestError.UnAuthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RequiredArgsConstructor
@RestControllerAdvice
public class ErrorExceptionControllerAdvice {

    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<ErrorEntity> exceptionHandler(final BadRequestException e) {
        return ResponseEntity
                .status(e.getErrorCode().getHttpStatus())
                .body(ErrorEntity.builder()
                        .code(e.getErrorCode().getCode())
                        .message(e.getErrorCode().getMessage())
                        .build());
    }

    @ExceptionHandler({ExpiredRefreshTokenException.class})
    public ResponseEntity<ErrorEntity> exceptionHandler(final ExpiredRefreshTokenException e) {
        return ResponseEntity
                .status(e.getErrorCode().getHttpStatus())
                .body(ErrorEntity.builder()
                        .code(e.getErrorCode().getCode())
                        .message(e.getErrorCode().getMessage())
                        .build());
    }

    @ExceptionHandler({ExpiredAccessTokenException.class})
    public ResponseEntity<ErrorEntity> exceptionHandler(final ExpiredAccessTokenException e) {
        return ResponseEntity
                .status(e.getErrorCode().getHttpStatus())
                .body(ErrorEntity.builder()
                        .code(e.getErrorCode().getCode())
                        .message(e.getErrorCode().getMessage())
                        .build());
    }

    @ExceptionHandler({UnAuthorizedException.class})
    public ResponseEntity<ErrorEntity> exceptionHandler(final UnAuthorizedException e) {
        return ResponseEntity
                .status(e.getErrorCode().getHttpStatus())
                .body(ErrorEntity.builder()
                        .code(e.getErrorCode().getCode())
                        .message(e.getErrorCode().getMessage())
                        .build());
    }

}
