package com.nbp.profileservice.messaging;

public interface ProfileUpdatedMessagePublisher {

    void publish(final String message);
}