package com.nbp.profileservice.messaging.impl;

import com.nbp.profileservice.messaging.ProfileUpdatedMessagePublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
public class RedisProfileUpdatedMessagePublisher implements ProfileUpdatedMessagePublisher {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private ChannelTopic profileUpdatedTopic;

    public RedisProfileUpdatedMessagePublisher() {
    }

    public RedisProfileUpdatedMessagePublisher(final RedisTemplate<String, Object> redisTemplate, final ChannelTopic profileUpdatedTopic) {
        this.redisTemplate = redisTemplate;
        this.profileUpdatedTopic = profileUpdatedTopic;
    }

    public void publish(final String message) {
        redisTemplate.convertAndSend(profileUpdatedTopic.getTopic(), message);
    }


}
