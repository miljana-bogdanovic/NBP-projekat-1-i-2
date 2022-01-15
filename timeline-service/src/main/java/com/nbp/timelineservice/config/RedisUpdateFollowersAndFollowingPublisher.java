package com.nbp.timelineservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
public class RedisUpdateFollowersAndFollowingPublisher {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private ChannelTopic profileFollowedTopic;

    public RedisUpdateFollowersAndFollowingPublisher() {
    }

    public RedisUpdateFollowersAndFollowingPublisher(final RedisTemplate<String, Object> redisTemplate, final ChannelTopic profileCreatedTopic) {
        this.redisTemplate = redisTemplate;
        this.profileFollowedTopic = profileCreatedTopic;
    }

    public void publish(final String message) {
        redisTemplate.convertAndSend(profileFollowedTopic.getTopic(), message);
    }


}
