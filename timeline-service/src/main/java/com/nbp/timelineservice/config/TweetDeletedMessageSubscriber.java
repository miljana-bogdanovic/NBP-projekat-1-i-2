package com.nbp.timelineservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.nbp.timelineservice.messaging.TweetCreated;
import com.nbp.timelineservice.model.cassandra.*;
import com.nbp.timelineservice.model.cassandra.retweets.PrimaryKeyRetweeted;
import com.nbp.timelineservice.model.cassandra.retweets.Retweeted;
import com.nbp.timelineservice.repository.RetweetedRepository;
import com.nbp.timelineservice.repository.TimelineRepository;
import com.nbp.timelineservice.repository.UserlineRepository;
import com.nbp.timelineservice.service.TimelineService;
import com.nbp.timelineservice.service.UserlineService;
import com.nbp.timelineservice.service.impl.UserFollowersService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TweetDeletedMessageSubscriber implements MessageListener {

    public static List<String> messageList = new ArrayList<>();

    @Autowired
    private TimelineRepository timelineRepository;
    @Autowired
    private UserlineRepository userlineRepository;
    @Autowired
    private UserFollowersService followersService;
    @Autowired
    private RetweetedRepository retweetedRepository;


    ObjectMapper mapper = new ObjectMapper();


    @SneakyThrows
    public void onMessage(final Message message, final byte[] pattern) {
        messageList.add(message.toString());
        System.out.println("Message received: " + new String(message.getBody()));
        TweetCreated tweetCreated = mapper.readValue(message.getBody(), TweetCreated.class);
        userlineRepository.
                deleteById(PrimaryKeyUserline.builder().username(tweetCreated.getUsername()).createdAt(tweetCreated.getCreatedAt()).build());
        List<UserFollowers> followersForUser = followersService.getFollowersForUser(tweetCreated.getUsername());
        List<String> pks = followersForUser.stream().map(t -> t.getPrimaryKey().getFollowerUsername()).collect(Collectors.toList());
        //timelineRepository.deleteTweet(pks, tweetCreated.getUsername(), tweetCreated.getCreatedAt());
        List<Timeline> collect = timelineRepository
                .findAll()
                .stream()
                .filter(t -> pks.contains(t.getPrimaryKey().getUsername()))
                .filter(t -> t.getPrimaryKey().getOriginalOwnerUsername().equals(tweetCreated.getUsername()))
                .filter(t -> t.getPrimaryKey().getCreatedAt().equals(tweetCreated.getCreatedAt()))
                .collect(Collectors.toList());

        Boolean b=false;
        Retweeted tweet = null;
        for(Timeline t : collect){
            if(t.getIsRetweet()){
                b=true;
                Calendar c = Calendar.getInstance();
                c.setTime(tweetCreated.getCreatedAt());
                //c.add(Calendar.YEAR, 1);
                //c.add(Calendar.HOUR, -1);
                Date newDate = c.getTime();
                List<Retweeted> collect1 = retweetedRepository.findAll().stream()
                        .filter(r -> r.getRetweetCreatedAt().equals(newDate))
                        .filter(r -> r.getPrimaryKeyRetweeted().getUsernameWhoRetweetedTheTweet()
                                .equals(tweetCreated.getUsername()))
                        .collect(Collectors.toList());

                for (Retweeted r : collect1){
                    tweet=r;
                    retweetedRepository.delete(r);
                }
            }
            timelineRepository.delete(t);
        }
        if (b){
            Calendar c = Calendar.getInstance();
            c.setTime(tweet.getPrimaryKeyRetweeted().getCreatedAt());
            //c.add(Calendar.YEAR, 1);
            c.add(Calendar.HOUR, 1);
            Date newDate = c.getTime();

            List<Userline> userline = userlineRepository.findByPartitionKeyAndCreatedAt(tweetCreated.getRetweetedFrom(), newDate);
            Userline userline1 = userline.get(0);
            userline1.setRetweets(userline1.getRetweets()-1);
            userlineRepository.save(userline1);

            List<UserFollowers> followersForUserName = followersService.getFollowersForUser(tweetCreated.getRetweetedFrom());
            for (UserFollowers userFollowers : followersForUserName) {
                List<Timeline> timeline = timelineRepository.findByPartitionKeyAndCreatedAt(userFollowers.getPrimaryKey().getFollowerUsername(),newDate);
                Timeline timeline1 = timeline.get(0);
                timeline1.setRetweets(timeline1.getRetweets()-1);
                timelineRepository.save(timeline1);
            }

//            List<Retweeted> collect1 = retweetedRepository.findAll().stream()
//                    .filter(r -> r.getPrimaryKeyRetweeted().getCreatedAt().equals(tweetCreated.getCreatedAt()))
//                    .filter(r -> r.getPrimaryKeyRetweeted().getUsername()
//                            .equals(tweetCreated.getRetweetedFrom()))
//                    .collect(Collectors.toList());
//            for (Retweeted retweeted : collect1) {
//
//            }

        }

    }
}
