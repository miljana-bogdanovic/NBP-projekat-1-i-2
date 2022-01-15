package com.nbp.profileservice.messaging;

public interface ProfileCreatedMessagePublisher {

    void publish(final String message);
}