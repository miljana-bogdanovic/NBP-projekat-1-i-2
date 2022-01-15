package com.nbp.tweetsservice.messaging.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nbp.tweetsservice.dto.cassandra.TweetEntity;
import com.nbp.tweetsservice.dto.redis.TweetMessage;
import com.nbp.tweetsservice.repositories.TweetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class RedisMessageSubscriber implements MessageListener {

    public static List<String> messageList = new ArrayList<String>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private TweetRepository tweetRepository;

    public void onMessage(final Message message, final byte[] pattern) {
       messageList.add(message.toString());
       System.out.println("Message received: " + new String(message.getBody()));

       tweetRepository.deleteByPrimaryKeyUsername(message.toString());
    }
}
