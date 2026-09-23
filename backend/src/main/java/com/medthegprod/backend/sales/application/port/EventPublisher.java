package com.medthegprod.backend.sales.application.port;

public interface EventPublisher {

    void publish(Object event);
}