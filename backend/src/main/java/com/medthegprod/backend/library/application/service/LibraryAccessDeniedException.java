package com.medthegprod.backend.library.application.service;

public class LibraryAccessDeniedException extends RuntimeException {

    public LibraryAccessDeniedException(String message) {
        super(message);
    }
}