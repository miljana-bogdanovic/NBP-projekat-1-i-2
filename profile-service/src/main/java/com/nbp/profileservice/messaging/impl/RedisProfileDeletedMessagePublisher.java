package com.nbp.profileservice.messaging.impl;

import com.nbp.profileservice.messaging.ProfileDeletedMessagePublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
public class RedisProfileDeletedMessagePublisher implements ProfileDeletedMessagePublisher {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private ChannelTopic profileDeletedTopic;

    public RedisProfileDeletedMessagePublisher() {
    }

    public RedisProfileDeletedMessagePublisher(final RedisTemplate<String, Object> redisTemplate, final ChannelTopic profileDeletedTopic) {
        this.redisTemplate = redisTemplate;
        this.profileDeletedTopic = profileDeletedTopic;
    }

    public void publish(final String message) {
        redisTemplate.convertAndSend(profileDeletedTopic.getTopic(), message);
    }


}
