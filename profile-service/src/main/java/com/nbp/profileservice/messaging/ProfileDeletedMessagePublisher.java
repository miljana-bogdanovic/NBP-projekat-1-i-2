package com.nbp.profileservice.messaging;

public interface ProfileDeletedMessagePublisher {

    void publish(final String message);
}