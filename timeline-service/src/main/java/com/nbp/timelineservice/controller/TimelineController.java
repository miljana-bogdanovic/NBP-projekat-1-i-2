package com.nbp.timelineservice.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nbp.timelineservice.config.RedisUnfollowProfile;
import com.nbp.timelineservice.config.RedisUpdateFollowersAndFollowingPublisher;
import com.nbp.timelineservice.model.cassandra.Timeline;
import com.nbp.timelineservice.model.cassandra.Userline;
import com.nbp.timelineservice.model.cassandra.likes.Liked;
import com.nbp.timelineservice.model.cassandra.likes.PrimaryKeyLiked;
import com.nbp.timelineservice.model.cassandra.retweets.PrimaryKeyRetweeted;
import com.nbp.timelineservice.model.cassandra.retweets.Retweeted;
import com.nbp.timelineservice.model.domain.TimelineDto;
import com.nbp.timelineservice.model.domain.UserFollowersDto;
import com.nbp.timelineservice.model.domain.UserFriendsDto;
import com.nbp.timelineservice.model.domain.UserlineDto;
import com.nbp.timelineservice.repository.LikedRepository;
import com.nbp.timelineservice.repository.RetweetedRepository;
import com.nbp.timelineservice.repository.TimelineRepository;
import com.nbp.timelineservice.repository.UserlineRepository;
import com.nbp.timelineservice.service.RedisLockService;
import com.nbp.timelineservice.service.TimelineService;
import com.nbp.timelineservice.service.UserlineService;
import com.nbp.timelineservice.service.impl.UserFollowersService;
import com.nbp.timelineservice.service.impl.UserFriendsService;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
public class TimelineController {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserlineService userlineService;

    @Autowired
    private UserlineRepository userlineRepository;

    @Autowired
    private TimelineService timelineService;

    @Autowired
    private TimelineRepository timelineRepository;

    @Autowired
    private UserFriendsService userFriendsService;

    @Autowired
    private UserFollowersService userFollowersService;

    @Autowired
    private RedisUpdateFollowersAndFollowingPublisher publisher;

    @Autowired
    private RedisUnfollowProfile unfollowProfile;

    @Autowired
    private LikedRepository likedRepository;

    @Autowired
    private RetweetedRepository retweetedRepository;

    @GetMapping("/timeline/{username}")
    public ResponseEntity<List<TimelineDto>> getTimeline(@PathVariable String username){
        return ResponseEntity.ok(timelineService.getTimelineForUser(username));
    }

    @GetMapping("/userline/{username}")
    public ResponseEntity<List<UserlineDto>> getUserline(@PathVariable String username){
        return ResponseEntity.ok(userlineService.getUserlineDto(username));
    }

    @GetMapping("/{username}/followers")
    public ResponseEntity<List<UserFollowersDto>> getUserFollowers(@PathVariable String username){
        return ResponseEntity.ok(userFollowersService.getFollowersForUserDto(username));
    }

    @GetMapping("/{username}/following")
    public ResponseEntity<List<UserFriendsDto>> getUserFollowing(@PathVariable String username){
        return ResponseEntity.ok(userFriendsService.getFollowersForUserDto(username));
    }

    @GetMapping("/tweets/{username}")
    public ResponseEntity<List<UserlineDto>> getAllTweets(@PathVariable String username){
        return ResponseEntity.ok(userlineService.getAllTweets(username));
    }


    @SneakyThrows
    @PostMapping("/retweet/createdAt/{createdAt}/username/{username}/retweetedFrom/{retweetedFrom}/retweetCreatedAt/{retweetCreatedAt}")
    public void retweet(@PathVariable String username, @PathVariable String createdAt, @PathVariable String retweetedFrom, @PathVariable String retweetCreatedAt){
        Date parse = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(createdAt);
        Date parse2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(retweetCreatedAt);

        Calendar c = Calendar.getInstance();
        c.setTime(parse);
        //c.add(Calendar.YEAR, 1);
        c.add(Calendar.HOUR, 1);
        Date newDate = c.getTime();
        c.setTime(parse2);
        c.add(Calendar.HOUR, 1);
        Date newDate2 = c.getTime();

        Optional<Userline> first = userlineRepository.findByPartitionKey(retweetedFrom)
                .stream()
                .filter(t -> t.getPrimaryKey().getCreatedAt().equals(newDate)).findFirst();

        Optional<Timeline> ti = timelineRepository.findByPartitionKey(username)
                .stream()
                .filter(t -> t.getPrimaryKey().getCreatedAt().equals(newDate))
                .findFirst();

        Timeline timeline1 = ti.get();
        timeline1.setIsRetweetedByUser(true);
        timeline1.setRetweets(timeline1.getRetweets() == null ? 1 :  timeline1.getRetweets()+1);
        timelineRepository.save(timeline1);

        Userline timeline = first.get();
        timeline.setIsRetweetedByUser(true);
        timeline.setRetweets(timeline.getRetweets() == null ? 1 :  timeline.getRetweets()+1);
        userlineRepository.save(timeline);

        retweetedRepository.save(
                Retweeted.builder().primaryKeyRetweeted(
                        PrimaryKeyRetweeted
                                .builder()
                                .createdAt(parse) //OVO MOZDA TREBA NEW DATE
                                .username(retweetedFrom)
                                .usernameWhoRetweetedTheTweet(username)
                                .build())
                        .retweetCreatedAt(newDate2)
                        .build());

    }

    @SneakyThrows
    @PostMapping("/retweet/deleted/createdAt/{createdAt}/username/{username}/retweetedFrom/{retweetedFrom}")
    public void retweetDeleted(@PathVariable String username, @PathVariable String createdAt, @PathVariable String retweetedFrom){
        Date parse = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(createdAt);

        Calendar c = Calendar.getInstance();
        c.setTime(parse);
        //c.add(Calendar.YEAR, 1);
        c.add(Calendar.HOUR, 1);
        Date newDate = c.getTime();

        Optional<Timeline> first = timelineRepository.findByPartitionKeyAndCreatedAt(username, newDate)
                .stream()
                //.filter(t -> t.getPrimaryKey().getCreatedAt().equals(newDate))
                .filter(t -> t.getPrimaryKey().getOriginalOwnerUsername().equals(retweetedFrom))
                .findFirst();

        Timeline timeline = first.get();

        if(timeline.getRetweets() == null){
            timeline.setRetweets(0);
        } else if(timeline.getRetweets().equals(0)) {
            timeline.setRetweets(0);
        } else if (timeline.getRetweets() > 0) {
            timeline.setRetweets(timeline.getRetweets() - 1 );
        }

        timeline.setIsRetweetedByUser(false);

        timelineRepository.save(timeline);


    }

    @PostMapping("/{username}/follow/{follows}")
    public void followUser(@PathVariable String username, @PathVariable String follows) throws JsonProcessingException {
        userFriendsService.addFriendForUser(username, follows);
        userFollowersService.addFollowerForUser(username, follows);
        timelineService.addTweetsToTimeline(username, follows);
        publisher.publish(objectMapper.writeValueAsString(Dto.builder().follows(follows).username(username).build()));
    }

    @PostMapping("/{username}/unfollow/{unfollows}")
    public void unfollowUser(@PathVariable String username, @PathVariable String unfollows) throws JsonProcessingException {
        userFriendsService.removeFriendForUser(username, unfollows);
        userFollowersService.removeFollowerForUser(username, unfollows);
        timelineService.removeTweetsFromTimeline(username, unfollows);
        unfollowProfile.publish(objectMapper.writeValueAsString(Dto.builder().follows(unfollows).username(username).build()));
    }

    @PostMapping(path = "/like")
    public void likeTweet(@RequestBody TweetDto tweetLikedDto) {
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.afterPropertiesSet();
        RedisLockRegistry registry = new RedisLockRegistry(jedisConnectionFactory, "lock");
        RedisLockService redisLockService = new RedisLockService(registry);
        userlineService.getUserline(tweetLikedDto.originalOwnerUsername)
                .stream()
                .filter(t -> t.getPrimaryKey().getCreatedAt().equals(tweetLikedDto.createdAt))
                .findAny()
                .ifPresent(t -> {
                    t.setLikes(t.getLikes()+1);
                    if(t.getPrimaryKey().getUsername().equals(tweetLikedDto.username))
                        t.setIsLikedByUser(true);
                    userlineRepository.save(t);
        });
        List<Timeline> collect = timelineRepository.findAll()
                .stream()
                .filter(timeline -> timeline.getPrimaryKey().getOriginalOwnerUsername()
                        .equals(tweetLikedDto.originalOwnerUsername))
                .filter(timeline -> timeline.getPrimaryKey().getCreatedAt().equals(tweetLikedDto.createdAt))
                .collect(Collectors.toList());
        for(Timeline t : collect){
            redisLockService.lock("lock");
            t.setLikes(t.getLikes()+1);
            if(t.getPrimaryKey().getUsername().equals(tweetLikedDto.username))
                t.setIsLikedByUser(true);
            timelineRepository.save(t);
            redisLockService.unlock("lock");
        }

        likedRepository.save(Liked.builder().primaryKeyLiked(
                PrimaryKeyLiked.builder()
                .createdAt(tweetLikedDto.createdAt)
                .username(tweetLikedDto.originalOwnerUsername)
                .usernameWhoLikesTheTweet(tweetLikedDto.username)
                .build()).build());
    }

    @PostMapping(path = "/unlike")
    public void unlikeTweet(@RequestBody TweetDto tweetLikedDto) {
        userlineService.getUserline(tweetLikedDto.originalOwnerUsername)
                .stream()
                .filter(t -> t.getPrimaryKey().getCreatedAt().equals(tweetLikedDto.createdAt))
                .findAny().ifPresent(t -> {
            t.setLikes(t.getLikes()-1);
            if(t.getPrimaryKey().getUsername().equals(tweetLikedDto.username))
                t.setIsLikedByUser(false);
            userlineRepository.save(t);
        });
        List<Timeline> collect = timelineRepository.findAll()
                .stream()
                .filter(timeline -> timeline.getPrimaryKey().getOriginalOwnerUsername()
                        .equals(tweetLikedDto.originalOwnerUsername))
                .filter(timeline -> timeline.getPrimaryKey().getCreatedAt().equals(tweetLikedDto.createdAt))
                .collect(Collectors.toList());
        for(Timeline t : collect){
            t.setLikes(t.getLikes()-1);
            if(t.getPrimaryKey().getUsername().equals(tweetLikedDto.username))
                t.setIsLikedByUser(false);
            timelineRepository.save(t);
        }

        likedRepository.delete(Liked.builder().primaryKeyLiked(
                PrimaryKeyLiked.builder()
                        .createdAt(tweetLikedDto.createdAt)
                        .username(tweetLikedDto.originalOwnerUsername)
                        .usernameWhoLikesTheTweet(tweetLikedDto.username)
                        .build()).build());
    }

    @GetMapping("{username}/follows/{follows}")
    public ResponseEntity<Boolean> followsUser(@PathVariable String username, @PathVariable String follows){
        return ResponseEntity.ok(userFriendsService
                .getFollowersForUser(username)
                .stream()
                .map(t -> t.getPrimaryKey().getFriendUsername())
                .collect(Collectors.toList()).contains(follows));
    }

    @SneakyThrows
    @PostMapping("/liked/createdAt/{createdAt}/username/{username}/originalOwnerUsername/{originalOwnerUsername}")
    public ResponseEntity<Boolean> isLikedByUser(@PathVariable String username, @PathVariable String createdAt, @PathVariable String originalOwnerUsername){
        Date parse = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(createdAt);

        Calendar c = Calendar.getInstance();
        c.setTime(parse);
        //c.add(Calendar.YEAR, 1);
        c.add(Calendar.HOUR, 1);
        Date newDate = c.getTime();

        Optional<Liked> byId = likedRepository.findById(PrimaryKeyLiked.builder()
                .usernameWhoLikesTheTweet(username).username(originalOwnerUsername).createdAt(newDate).build());

        return ResponseEntity.ok(byId.isPresent());
    }

    @SneakyThrows
    @PostMapping("/retweeted/createdAt/{createdAt}/username/{username}/originalOwnerUsername/{originalOwnerUsername}")
    public ResponseEntity<Boolean> isRetweetedByUser(@PathVariable String username, @PathVariable String createdAt, @PathVariable String originalOwnerUsername){
        Date parse = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(createdAt);

        Calendar c = Calendar.getInstance();
        c.setTime(parse);
        //c.add(Calendar.YEAR, 1);
        c.add(Calendar.HOUR, 1);
        Date newDate = c.getTime();

        Optional<Retweeted> byId = retweetedRepository.findById(PrimaryKeyRetweeted.builder()
                .usernameWhoRetweetedTheTweet(username).username(originalOwnerUsername).createdAt(parse).build());

        return ResponseEntity.ok(byId.isPresent());
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

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    static class TweetDto {
        private String username;
        private String originalOwnerUsername;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        private Date createdAt;
    }
}
