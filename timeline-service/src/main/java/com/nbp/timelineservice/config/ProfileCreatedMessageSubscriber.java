package com.nbp.timelineservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nbp.timelineservice.messaging.TweetCreated;
import com.nbp.timelineservice.model.cassandra.PrimaryKeyTimeline;
import com.nbp.timelineservice.model.cassandra.Timeline;
import com.nbp.timelineservice.model.cassandra.UserFollowers;
import com.nbp.timelineservice.model.cassandra.Userline;
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

@Component
public class ProfileCreatedMessageSubscriber implements MessageListener {

    public static List<String> messageList = new ArrayList<>();

    @Autowired
    private TimelineService timelineService;
    @Autowired
    private UserlineService userlineService;
    @Autowired
    private UserFollowersService followersService;


    ObjectMapper mapper = new ObjectMapper();
    private final ObjectMapper objectMapper = new ObjectMapper();


    public void onMessage(final Message message, final byte[] pattern) {
        messageList.add(message.toString());
        System.out.println("Message received: " + new String(message.getBody()));
/*        messageList.add(message.toString());
        System.out.println("Message received: " + new String(message.getBody()));
        TweetCreated tweetMessage = null;
        String s = message.toString();
        try {
            tweetMessage = objectMapper.readValue(message.getBody(), TweetCreated.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Timeline timeline = new Timeline();
        timeline.setBody(tweetMessage.getBody());
        timeline.setCreatorUsername(tweetMessage.getUsername());
        timeline.setEmail("");
        PrimaryKeyTimeline pkt = new PrimaryKeyTimeline();
        pkt.setUsername(tweetMessage.getUsername());
        pkt.setCreatedAt(tweetMessage.getCreatedAt());
        timeline.setPrimaryKey(pkt);

        timelineService.save(timeline);

        List<UserFollowers> followersForUser = followersService.getFollowersForUser(timeline.getCreatorUsername());
        followersForUser.forEach(t -> {
            //pitanje je da l moye ovako
            Userline userline = mapper.convertValue(timeline, Userline.class);
            userlineService.save(userline);
        });*/
    }
}
