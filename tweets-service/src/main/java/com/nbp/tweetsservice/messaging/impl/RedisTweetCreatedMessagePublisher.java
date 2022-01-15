package com.nbp.tweetsservice.messaging.impl;

import com.nbp.tweetsservice.messaging.TweetCreatedMessagePublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
public class RedisTweetCreatedMessagePublisher implements TweetCreatedMessagePublisher {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private ChannelTopic tweetCreatedTopic;

    public RedisTweetCreatedMessagePublisher() {
    }

    public RedisTweetCreatedMessagePublisher(final RedisTemplate<String, Object> redisTemplate, final ChannelTopic tweetCreatedTopic) {
        this.redisTemplate = redisTemplate;
        this.tweetCreatedTopic = tweetCreatedTopic;
    }

    public void publish(final String message) {
        redisTemplate.convertAndSend(tweetCreatedTopic.getTopic(), message);
    }


}
