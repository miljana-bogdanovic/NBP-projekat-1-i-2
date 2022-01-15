package com.nbp.tweetsservice.messaging.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nbp.tweetsservice.dto.cassandra.TweetEntity;
import com.nbp.tweetsservice.repositories.TweetRepository;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProfileUpdatedMessageSubscriber implements MessageListener {

    public static List<String> messageList = new ArrayList<String>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private TweetRepository tweetRepository;

    @SneakyThrows
    public void onMessage(final Message message, final byte[] pattern) {
       messageList.add(message.toString());
       System.out.println("Message received: " + new String(message.getBody()));


       Profile profile = objectMapper.readValue(message.getBody(), Profile.class);


        List<TweetEntity> tweets = tweetRepository
                .findByPrimaryKeyUsername(profile.username);

        if(tweets != null && !tweets.isEmpty()){
            for(TweetEntity t : tweets){
                t.setPhoto(profile.image);
                t.setFirstName(profile.firstName);
                t.setLastName(profile.lastName);
                tweetRepository.save(t);
            }
        }

        //TODO: SREDI MODEL PODATAKA OVDE TREBA DA IZVADIS SVE RETVITOVE KAD TO SREDIS SAMO IH APPENDUJ NA ONU PRETHODNU LISTU
       //tweetRepository.findByOriginalCreatorUsername()
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
