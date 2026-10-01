package com.medthegprod.backend.catalog.application.port;

public interface EventPublisher {

    void publish(Object event);
}