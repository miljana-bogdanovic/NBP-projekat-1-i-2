package com.nbp.timelineservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.nbp.timelineservice.messaging.TweetCreated;
import com.nbp.timelineservice.model.cassandra.*;
import com.nbp.timelineservice.repository.TimelineRepository;
import com.nbp.timelineservice.repository.UserFollowersRepository;
import com.nbp.timelineservice.repository.UserFriendsRepository;
import com.nbp.timelineservice.repository.UserlineRepository;
import com.nbp.timelineservice.service.RedisLockService;
import com.nbp.timelineservice.service.TimelineService;
import com.nbp.timelineservice.service.UserlineService;
import com.nbp.timelineservice.service.impl.UserFollowersService;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfileUpdatedMessageSubscriber implements MessageListener {

    public static List<String> messageList = new ArrayList<>();

    @Autowired
    private TimelineRepository timelineRepository;
    @Autowired
    private UserlineRepository userlineRepository;
    @Autowired
    private UserFollowersRepository userFollowersRepository;
    @Autowired
    private UserFriendsRepository userFriendsRepository;

    ObjectMapper mapper = new ObjectMapper();
    JsonMapper jsonMapper = new JsonMapper();


    @SneakyThrows
    public void onMessage(final Message message, final byte[] pattern) {
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.afterPropertiesSet();
        RedisLockRegistry registry = new RedisLockRegistry(jedisConnectionFactory, "lock");
        RedisLockService redisLockService = new RedisLockService(registry);
        messageList.add(message.toString());
        System.out.println("Message received: " + new String(message.getBody()));

        Profile profile = mapper.readValue(message.getBody(), Profile.class);

        List<Timeline> timelines = timelineRepository
                .findAll()
                .stream()
                .filter(t -> t.getPrimaryKey().getOriginalOwnerUsername().equals(profile.username))
                .collect(Collectors.toList());

        if(!timelines.isEmpty()){
            for(Timeline t : timelines) {
                redisLockService.lock("lock");
                t.setPhoto(profile.image);
                t.setFirstName(profile.firstName);
                t.setLastName(profile.lastName);
                timelineRepository.save(t);
                redisLockService.unlock("lock");
            }
        }

        List<Userline> userlines = userlineRepository.findByPartitionKey(profile.username);
        if(userlines != null && !userlines.isEmpty()){
            for(Userline t : userlines) {
                t.setPhoto(profile.image);
                t.setFirstName(profile.firstName);
                t.setLastName(profile.lastName);
                userlineRepository.save(t);
            }
        }

        List<UserFollowers> userFollowers = userFollowersRepository.findByPrimaryKeyFollowerUsername(profile.username);
        if(userFollowers != null && !userFollowers.isEmpty()){
            for(UserFollowers t : userFollowers) {
                t.setPhoto(profile.image);
                t.setFirstName(profile.firstName);
                t.setLastName(profile.lastName);
                userFollowersRepository.save(t);
            }
        }

        List<UserFriends> userFriends = userFriendsRepository.findByPrimaryKeyFriendUsername(profile.username);
        if(userFriends != null && !userFriends.isEmpty()){
            for(UserFriends t : userFriends) {
                t.setPhoto(profile.image);
                t.setFirstName(profile.firstName);
                t.setLastName(profile.lastName);
                userFriendsRepository.save(t);
            }
        }
    }

            @Data
            @Builder
            @AllArgsConstructor
            @NoArgsConstructor
            public static class Profile {

                private String username;

                private Integer followers;

                private String following;

                private String firstName;

                private String lastName;

                private String email;

                private String password;

                private String image;

            }
}
