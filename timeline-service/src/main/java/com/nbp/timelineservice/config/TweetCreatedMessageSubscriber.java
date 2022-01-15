package com.nbp.timelineservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nbp.timelineservice.messaging.TweetCreated;
import com.nbp.timelineservice.model.cassandra.*;
import com.nbp.timelineservice.repository.TimelineRepository;
import com.nbp.timelineservice.repository.UserlineRepository;
import com.nbp.timelineservice.service.TimelineService;
import com.nbp.timelineservice.service.UserlineService;
import com.nbp.timelineservice.service.impl.UserFollowersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TweetCreatedMessageSubscriber implements MessageListener {

    public static List<String> messageList = new ArrayList<>();

    @Autowired
    private TimelineService timelineService;
    @Autowired
    private UserlineService userlineService;
    @Autowired
    private UserFollowersService followersService;

    @Autowired private TimelineRepository timelineRepository;
    @Autowired private UserlineRepository userlineRepository;


    ObjectMapper mapper = new ObjectMapper();
    private final ObjectMapper objectMapper = new ObjectMapper();


    public void onMessage(final Message message, final byte[] pattern) {
        messageList.add(message.toString());
        System.out.println("Message received: " + new String(message.getBody()));
        TweetCreated tweetMessage = null;
        try {
            tweetMessage = objectMapper.readValue(message.getBody(), TweetCreated.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Userline userline = new Userline();

        PrimaryKeyUserline pku = PrimaryKeyUserline.builder().username(tweetMessage.getUsername()).createdAt(tweetMessage.getCreatedAt()).build();
        userline.setPrimaryKey(pku);
        userline.setBody(tweetMessage.getBody());
        userline.setPhoto(tweetMessage.getPhoto());
        userline.setIsRetweet(tweetMessage.getIsRetweet());
        userline.setRetweets(tweetMessage.getRetweets());
        userline.setFirstName(tweetMessage.getFirstName());
        userline.setLastName(tweetMessage.getLastName());
        userline.setOriginalOwnerUsername(tweetMessage.getOriginalOwnerUsername());
        userline.setLikes(tweetMessage.getLikes());
        userline.setRetweetedFrom(tweetMessage.getRetweetedFrom());
        userline.setIsRetweetedByUser(tweetMessage.getIsRetweetedByUser());

        userlineService.save(userline);

        List<UserFollowers> followersForUser = followersService.getFollowersForUser(userline.getPrimaryKey().getUsername());
        followersForUser.forEach(t -> {
            Timeline build = Timeline
                    .builder()
                    .body(userline.getBody())
                    .firstName(userline.getFirstName())
                    .lastName(userline.getLastName())
                    .isRetweet(userline.getIsRetweet())
                    .photo(userline.getPhoto())
                    .retweets(userline.getRetweets())
                    .primaryKey(PrimaryKeyTimeline.builder()
                            .createdAt(userline.getPrimaryKey().getCreatedAt())
                            .username(t.getPrimaryKey().getFollowerUsername())
                            .originalOwnerUsername(userline.getPrimaryKey().getUsername()).build()
                    )
                    .retweetedFrom(userline.getRetweetedFrom())
                    .isRetweetedByUser(userline.getIsRetweetedByUser())
                    .likes(userline.getLikes())
                    .build();

            timelineService.save(build);
        });

        if(userline.getIsRetweet() != null && userline.getIsRetweet()){
            if (userline.getPrimaryKey().getUsername().equals(userline.getRetweetedFrom())) {
                userline.setIsRetweetedByUser(true);
            }

            List<Userline> collect = userlineRepository
                    .findAll()
                    .stream()
                    .filter(t -> t.getRetweetedFrom().equals(userline.getRetweetedFrom()))
                    .filter(t -> t.getPrimaryKey().getCreatedAt().equals(userline.getPrimaryKey().getCreatedAt()))
                    .collect(Collectors.toList());

            collect.forEach(t->{
                int base = t.getRetweets() != null ? t.getRetweets() : 0;
                t.setRetweets(base + 1);
                userlineRepository.save(t);
            });

            List<Timeline> timelines = timelineRepository
                    .findAll()
                    .stream()
                    .filter(t -> t.getRetweetedFrom().equals(userline.getRetweetedFrom()))
                    .filter(t -> t.getPrimaryKey().getCreatedAt().equals(userline.getPrimaryKey().getCreatedAt()))
                    .collect(Collectors.toList());

            timelines.forEach(t->{
                int base = t.getRetweets() != null ? t.getRetweets() : 0;
                t.setRetweets(base + 1);
                timelineRepository.save(t);
            });
        }
    }
}
