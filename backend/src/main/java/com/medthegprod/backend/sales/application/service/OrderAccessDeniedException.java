package com.medthegprod.backend.sales.application.service;

public class OrderAccessDeniedException extends RuntimeException {

    public OrderAccessDeniedException() {
        super("You do not have access to this order");
    }
}