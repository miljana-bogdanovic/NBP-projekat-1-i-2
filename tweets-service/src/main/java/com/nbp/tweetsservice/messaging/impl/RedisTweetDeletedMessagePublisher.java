package com.nbp.tweetsservice.messaging.impl;

import com.nbp.tweetsservice.messaging.TweetDeletedMessagePublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
public class RedisTweetDeletedMessagePublisher implements TweetDeletedMessagePublisher {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private ChannelTopic tweetDeletedTopic;

    public RedisTweetDeletedMessagePublisher() {
    }

    public RedisTweetDeletedMessagePublisher(final RedisTemplate<String, Object> redisTemplate, final ChannelTopic tweetDeletedTopic) {
        this.redisTemplate = redisTemplate;
        this.tweetDeletedTopic = tweetDeletedTopic;
    }

    public void publish(final String message) {
        redisTemplate.convertAndSend(tweetDeletedTopic.getTopic(), message);
    }


}
