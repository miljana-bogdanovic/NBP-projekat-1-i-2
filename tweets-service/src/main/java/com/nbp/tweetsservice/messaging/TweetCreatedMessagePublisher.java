package com.nbp.tweetsservice.messaging;

public interface TweetCreatedMessagePublisher {

    void publish(final String message);
}