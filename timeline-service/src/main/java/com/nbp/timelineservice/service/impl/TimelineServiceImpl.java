package com.nbp.timelineservice.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nbp.timelineservice.model.cassandra.PrimaryKeyTimeline;
import com.nbp.timelineservice.model.cassandra.Timeline;
import com.nbp.timelineservice.model.cassandra.Userline;
import com.nbp.timelineservice.model.domain.TimelineDto;
import com.nbp.timelineservice.model.domain.UserlineDto;
import com.nbp.timelineservice.repository.TimelineRepository;
import com.nbp.timelineservice.service.RedisLockService;
import com.nbp.timelineservice.service.TimelineService;
import com.nbp.timelineservice.service.UserlineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TimelineServiceImpl implements TimelineService {
    @Autowired
    private TimelineRepository timelineRepository;

    @Autowired
    private UserlineService userlineService;

    ObjectMapper mapper = new ObjectMapper();

    @Override
    @Cacheable(value = "timelineCache")
    public List<TimelineDto> getTimelineForUser(String username) {
        List<Timeline> byPartitionKey = timelineRepository.findByPartitionKey(username);
        List<TimelineDto> dtos = new ArrayList<>();
        for(Timeline t : byPartitionKey){
            TimelineDto build = TimelineDto
                    .builder()
                    .body(t.getBody())
                    .firstName(t.getFirstName())
                    .lastName(t.getLastName())
                    .createdAt(t.getPrimaryKey().getCreatedAt())
                    .isRetweet(t.getIsRetweet())
                    .likes(t.getLikes())
                    .photo(t.getPhoto())
                    .isLikedByUser(t.getIsLikedByUser())
                    .retweets(t.getRetweets())
                    .username(t.getPrimaryKey().getUsername())
                    .originalOwnerUsername(t.getPrimaryKey().getOriginalOwnerUsername())
                    .retweetedFrom(t.getRetweetedFrom())
                    .isRetweetedByUser(t.getIsRetweetedByUser())
                    .build();
            dtos.add(build);
        }
        return dtos;
    }

    @Override
    public void save(Timeline timeline) {
        timelineRepository.save(timeline);
    }

    @Override
    public void addTweetsToTimeline(String username, String follows) {
        List<Userline> userline = userlineService.getUserline(follows);
        for(Userline line : userline) {
            Timeline build = Timeline
                    .builder()
                    .body(line.getBody())
                    .firstName(line.getFirstName())
                    .lastName(line.getLastName())
                    .isRetweet(line.getIsRetweet())
                    .photo(line.getPhoto())
                    .retweets(line.getRetweets())
                    .primaryKey(PrimaryKeyTimeline.builder()
                            .createdAt(line.getPrimaryKey().getCreatedAt())
                            .username(username)
                            .originalOwnerUsername(follows).build()
                    )
                    .retweetedFrom(line.getRetweetedFrom())
                    .likes(line.getLikes())
                    .build();
            //build.setRetweetedFrom();
            save(build);
        }
    }

    @Override
    public void removeTweetsFromTimeline(String username, String followedUsername) {
        List<Timeline> timelines = timelineRepository
                .findAll()
                .stream()
                .filter(t -> t.getPrimaryKey().getUsername().equals(username))
                .filter(t -> t.getPrimaryKey().getOriginalOwnerUsername().equals(followedUsername))
                .collect(Collectors.toList());
        for(Timeline t : timelines){
            timelineRepository.delete(t);
        }
        //timelineRepository.removePostsFromTimeline(username, followedUsername);
    }

    @Override
    public List<TimelineDto> getAllTweets(String username) {
        List<Timeline> timeline=timelineRepository.getAllTweets();
        List<TimelineDto> dtos = new ArrayList<>();
        for(Timeline t : timeline){
            if (t.getPrimaryKey().getOriginalOwnerUsername().compareTo(username)!=0){
                TimelineDto build = TimelineDto
                        .builder()
                        .body(t.getBody())
                        .firstName(t.getFirstName())
                        .lastName(t.getLastName())
                        .createdAt(t.getPrimaryKey().getCreatedAt())
                        .isRetweet(t.getIsRetweet())
                        .likes(t.getLikes())
                        .photo(t.getPhoto())
                        .isLikedByUser(t.getIsLikedByUser())
                        .retweets(t.getRetweets())
                        .username(t.getPrimaryKey().getUsername())
                        .originalOwnerUsername(t.getPrimaryKey().getOriginalOwnerUsername())
                        .retweetedFrom(t.getRetweetedFrom())
                        .isRetweetedByUser(t.getIsRetweetedByUser())
                        .build();
                dtos.add(build);
            }
        }
        return dtos;


    }

//    @Override
//    public void updateTweetBody(List<Timeline> collect, String body){
//        for (Timeline t : collect){
//            redisLockService.lock("lock");
//            t.setBody(body);
//            //t.setLikes(userline.getLikes());
//            //t.setPhoto(userline.getPhoto());
//            //t.setRetweets(userline.getRetweets());
//            timelineRepository.save(t);
//            redisLockService.unlock("lock");
//        }
//    }

//    @Override
//    public void updatePhotoFirstNameLastName(List<Timeline> timelines, String firstName, String lastName, String image) {
//        if(!timelines.isEmpty()){
//            for(Timeline t : timelines) {
//                redisLockService.lock("lock");
//                t.setPhoto(image);
//                t.setFirstName(firstName);
//                t.setLastName(lastName);
//                timelineRepository.save(t);
//                redisLockService.unlock("lock");
//            }
//        }
    }

