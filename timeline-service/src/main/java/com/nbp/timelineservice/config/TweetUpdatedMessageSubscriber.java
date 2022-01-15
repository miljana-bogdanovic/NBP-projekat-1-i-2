package com.nbp.timelineservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nbp.timelineservice.messaging.TweetCreated;
import com.nbp.timelineservice.model.cassandra.*;
import com.nbp.timelineservice.repository.TimelineRepository;
import com.nbp.timelineservice.service.RedisLockService;
import com.nbp.timelineservice.repository.UserlineRepository;
import com.nbp.timelineservice.service.TimelineService;
import com.nbp.timelineservice.service.UserlineService;
import com.nbp.timelineservice.service.impl.UserFollowersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TweetUpdatedMessageSubscriber implements MessageListener {

    public static List<String> messageList = new ArrayList<>();

    @Autowired
    private TimelineService timelineService;

    @Autowired
    private TimelineRepository timelineRepository;
    @Autowired
    private UserlineRepository userlineRepository;
    @Autowired
    private UserlineService userlineService;
    @Autowired
    private UserFollowersService followersService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void onMessage(final Message message, final byte[] pattern) {
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.afterPropertiesSet();
        RedisLockRegistry registry = new RedisLockRegistry(jedisConnectionFactory, "lock");
        RedisLockService redisLockService = new RedisLockService(registry);        messageList.add(message.toString());
        System.out.println("Message received: " + new String(message.getBody()));
        TweetCreated tweetMessage = null;
        try {
            tweetMessage = objectMapper.readValue(message.getBody(), TweetCreated.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

//        Userline userline = new Userline();
//        userline.setBody(tweetMessage.getBody());
//        PrimaryKeyUserline pku = PrimaryKeyUserline.builder().username(tweetMessage.getUsername()).createdAt(tweetMessage.getCreatedAt()).build();
//        userline.setPrimaryKey(pku);
//        userline.setRetweets(tweetMessage.getRetweets());
//        userline.setLikes(tweetMessage.getLikes());
//        userline.setPhoto(tweetMessage.getPhoto());
//        userline.setIsRetweet(tweetMessage.getIsRetweet());
//        userline.setFirstName(tweetMessage.getFirstName());
//        userline.setLastName(tweetMessage.getLastName());
//        userline.setOriginalOwnerUsername(tweetMessage.getOriginalOwnerUsername());
//        userline.setRetweetedFrom(tweetMessage.getRetweetedFrom());
        PrimaryKeyUserline pku = PrimaryKeyUserline.builder().username(tweetMessage.getUsername()).createdAt(tweetMessage.getCreatedAt()).build();
        Userline userline = userlineRepository.findByPk(pku.getUsername(), pku.getCreatedAt());
        userline.setBody(tweetMessage.getBody());

        userlineService.save(userline);


        TweetCreated finalTweetMessage = tweetMessage;
        List<Timeline> collect = timelineRepository.findAll()
                .stream()
                .filter(t -> t.getPrimaryKey().getOriginalOwnerUsername()
                        .equals(finalTweetMessage.getUsername())).collect(Collectors.toList());

        //timelineService.updateTweetBody(collect, userline.getBody());
        for (Timeline t : collect){
            redisLockService.lock("lock");
            t.setBody(userline.getBody());
                //t.setLikes(userline.getLikes());
                //t.setPhoto(userline.getPhoto());
                //t.setRetweets(userline.getRetweets());
            timelineRepository.save(t);
            redisLockService.unlock("lock");
        }
    }
}
