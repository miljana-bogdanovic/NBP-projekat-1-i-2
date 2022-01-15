package com.nbp.tweetsservice.messaging;

public interface TweetDeletedMessagePublisher {

    void publish(final String message);
}