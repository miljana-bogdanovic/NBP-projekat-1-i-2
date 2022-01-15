package com.nbp.profileservice.messaging.impl;

import com.nbp.profileservice.messaging.ProfileCreatedMessagePublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
public class RedisProfileCreatedMessagePublisher implements ProfileCreatedMessagePublisher {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private ChannelTopic profileCreatedTopic;

    public RedisProfileCreatedMessagePublisher() {
    }

    public RedisProfileCreatedMessagePublisher(final RedisTemplate<String, Object> redisTemplate, final ChannelTopic profileCreatedTopic) {
        this.redisTemplate = redisTemplate;
        this.profileCreatedTopic = profileCreatedTopic;
    }

    public void publish(final String message) {
        redisTemplate.convertAndSend(profileCreatedTopic.getTopic(), message);
    }


}
