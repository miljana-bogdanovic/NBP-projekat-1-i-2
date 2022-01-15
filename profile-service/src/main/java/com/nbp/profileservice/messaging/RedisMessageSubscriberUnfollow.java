package com.nbp.profileservice.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nbp.profileservice.dto.cassandra.ProfileEntity;
import com.nbp.profileservice.repository.ProfileRepository;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RedisMessageSubscriberUnfollow implements MessageListener {

    public static List<String> messageList = new ArrayList<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private ProfileRepository profileRepository;

    @SneakyThrows
    public void onMessage(final Message message, final byte[] pattern) {
        messageList.add(message.toString());
        System.out.println("Message received: " + new String(message.getBody()));
        Dto dto = objectMapper.readValue(message.getBody(), Dto.class);
        ProfileEntity followed = profileRepository.findByUsername(dto.follows);
        followed.setFollowers(followed.getFollowers() - 1);
        profileRepository.save(followed);
        ProfileEntity follows = profileRepository.findByUsername(dto.username);
        follows.setFollowing(String.valueOf(Integer.parseInt(follows.getFollowing()) - 1));
        profileRepository.save(follows);
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    static class Dto {
        private String username;
        private String follows;

    }
}
