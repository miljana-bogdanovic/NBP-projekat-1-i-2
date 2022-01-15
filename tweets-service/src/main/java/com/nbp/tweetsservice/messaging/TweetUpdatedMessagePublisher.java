package com.nbp.tweetsservice.messaging;

public interface TweetUpdatedMessagePublisher {

    void publish(final String message);
}