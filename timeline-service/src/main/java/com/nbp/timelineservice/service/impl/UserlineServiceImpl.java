package com.nbp.timelineservice.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nbp.timelineservice.model.cassandra.Userline;
import com.nbp.timelineservice.model.domain.UserlineDto;
import com.nbp.timelineservice.repository.TimelineRepository;
import com.nbp.timelineservice.repository.UserlineRepository;
import com.nbp.timelineservice.service.UserlineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserlineServiceImpl implements UserlineService {

    @Autowired
    private TimelineRepository timelineRepository;

    @Autowired
    private UserlineRepository userlineRepository;

    @Autowired
    private UserFollowersService userFollowersService;

    @Autowired
    private UserFriendsService userFriendsService;

    ObjectMapper mapper = new ObjectMapper();

    @Override
    public List<Userline> getUserline(String username) {
        return userlineRepository.findByPartitionKey(username);

    }

    @Override
    @Cacheable(value = "userlineCache")
    public List<UserlineDto> getUserlineDto(String username) {
        List<Userline> byPartitionKey = userlineRepository.findByPartitionKey(username);
        List<UserlineDto> dtos = new ArrayList<>();
        for(Userline t : byPartitionKey) {
            UserlineDto build = UserlineDto
                    .builder()
                    .body(t.getBody())
                    .firstName(t.getFirstName())
                    .lastName(t.getLastName())
                    .createdAt(t.getPrimaryKey().getCreatedAt())
                    .isRetweet(t.getIsRetweet())
                    .likes(t.getLikes())
                    .photo(t.getPhoto())
                    .retweets(t.getRetweets())
                    .isLikedByUser(t.getIsLikedByUser())
                    .username(t.getPrimaryKey().getUsername())
                    .originalOwnerUsername(t.getOriginalOwnerUsername())
                    .retweetedFrom(t.getRetweetedFrom())
                    .isRetweetedByUser(t.getIsRetweetedByUser())
                    .build();
            dtos.add(build);
        }
        return dtos;
    }

    @Override
    public List<UserlineDto> getAllTweets(String username) {
        List<Userline> timeline=userlineRepository.getAllTweets();
        List<UserlineDto> dtos = new ArrayList<>();
        for(Userline t : timeline){
            if (t.getOriginalOwnerUsername().compareTo(username)!=0){
                UserlineDto build = UserlineDto
                        .builder()
                        .body(t.getBody())
                        .firstName(t.getFirstName())
                        .lastName(t.getLastName())
                        .createdAt(t.getPrimaryKey().getCreatedAt())
                        .isRetweet(t.getIsRetweet())
                        .likes(t.getLikes())
                        .photo(t.getPhoto())
                        .retweets(t.getRetweets())
                        .isLikedByUser(t.getIsLikedByUser())
                        .username(t.getPrimaryKey().getUsername())
                        .originalOwnerUsername(t.getOriginalOwnerUsername())
                        .retweetedFrom(t.getRetweetedFrom())
                        .isRetweetedByUser(t.getIsRetweetedByUser())
                        .build();
                dtos.add(build);
            }
        }
        return dtos;


    }

    @Override
    public void save(Userline userline) {
        userlineRepository.save(userline);
    }

    @Override
    public void delete(String id) {
        userlineRepository.deleteUserline(id);
        timelineRepository.deleteTimeline(id);
        userFollowersService.deleteFollowersForUser(id);
        userFriendsService.deleteFriendsForUser(id);
    }




}
