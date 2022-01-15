package com.nbp.timelineservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
public class RedisUnfollowProfile {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private ChannelTopic profileUnfollowedTopic;

    public RedisUnfollowProfile() {
    }

    public RedisUnfollowProfile(final RedisTemplate<String, Object> redisTemplate, final ChannelTopic profileUnfollowedTopic) {
        this.redisTemplate = redisTemplate;
        this.profileUnfollowedTopic = profileUnfollowedTopic;
    }

    public void publish(final String message) {
        redisTemplate.convertAndSend(profileUnfollowedTopic.getTopic(), message);
    }


}
