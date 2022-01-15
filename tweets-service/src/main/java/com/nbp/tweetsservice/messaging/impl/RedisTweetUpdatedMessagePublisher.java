package com.nbp.tweetsservice.messaging.impl;

import com.nbp.tweetsservice.messaging.TweetUpdatedMessagePublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
public class RedisTweetUpdatedMessagePublisher implements TweetUpdatedMessagePublisher {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private ChannelTopic tweetUpdatedTopic;

    public RedisTweetUpdatedMessagePublisher() {
    }

    public RedisTweetUpdatedMessagePublisher(final RedisTemplate<String, Object> redisTemplate, final ChannelTopic tweetUpdatedTopic) {
        this.redisTemplate = redisTemplate;
        this.tweetUpdatedTopic = tweetUpdatedTopic;
    }

    public void publish(final String message) {
        redisTemplate.convertAndSend(tweetUpdatedTopic.getTopic(), message);
    }


}
