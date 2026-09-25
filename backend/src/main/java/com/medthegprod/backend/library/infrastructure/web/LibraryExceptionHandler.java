package com.medthegprod.backend.library.infrastructure.web;

import com.medthegprod.backend.library.application.service.LibraryAccessDeniedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class LibraryExceptionHandler {

    @ExceptionHandler(LibraryAccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleAccessDenied(
            LibraryAccessDeniedException exception) {

        return new ErrorResponse(
                "LIBRARY_ACCESS_DENIED",
                exception.getMessage());
    }

    public record ErrorResponse(
            String code,
            String message) {
    }
}