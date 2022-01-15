package com.nbp.api.gateway.controller;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

@RestController
public class TimelineServiceController {
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/timeline/{username}")
    public ResponseEntity<Object> getTimeline(@PathVariable String username){
        return restTemplate.getForEntity("http://localhost:8082/timeline/" + username, Object.class);
    }

    @GetMapping("/userline/{username}")
    public ResponseEntity<Object> getUserline(@PathVariable String username){
        return restTemplate.getForEntity("http://localhost:8082/userline/" + username, Object.class);
    }

    @GetMapping(path = "/tweets/{username}")
    public ResponseEntity<Object> getAllTweets(@PathVariable String username){
        return restTemplate.getForEntity("http://localhost:8082/tweets/" + username, Object.class);
    }
    @PostMapping("/{username}/follow/{follows}")
    public void followUser(@PathVariable String username, @PathVariable String follows){
        restTemplate.postForEntity("http://localhost:8082/" +
                username +
                "/follow/" +
                follows, null, Void.class);
    }

    @PostMapping("/{username}/unfollow/{unfollows}")
    public void unFollowUser(@PathVariable String username, @PathVariable String unfollows){
        restTemplate.postForEntity("http://localhost:8082/" +
                username +
                "/unfollow/" +
                unfollows, null, Void.class);
    }

    @GetMapping("/{username}/followers")
    public ResponseEntity<Object[]> getUserFollowers(@PathVariable String username){
        return restTemplate.getForEntity("http://localhost:8082/" + username + "/followers", Object[].class);
    }

    @GetMapping("/{username}/following")
    public ResponseEntity<Object[]> getUserFollowing(@PathVariable String username){
        return restTemplate.getForEntity("http://localhost:8082/" + username + "/following", Object[].class);
    }

    @GetMapping("/{username}/follows/{follows}")
    public ResponseEntity<Object> follows(@PathVariable String username, @PathVariable String follows){
        return restTemplate.getForEntity("http://localhost:8082/" +
                username +
                "/follows/" +
                follows, Object.class);
    }

    @PostMapping(path = "/like")
    public void likeTweet(@RequestBody Object tweetLikedDto) {
        restTemplate.postForEntity("http://localhost:8082/like",tweetLikedDto, Object.class);
    }

    @PostMapping(path = "/unlike")
    public void unlikeTweet(@RequestBody Object tweetUnlikedDto) {
        restTemplate.postForEntity("http://localhost:8082/unlike",tweetUnlikedDto, Object.class);
    }

    @PostMapping("/retweet/createdAt/{createdAt}/username/{username}/retweetedFrom/{retweetedFrom}/retweetCreatedAt/{retweetCreatedAt}")
    public void retweet(@PathVariable String username, @PathVariable String createdAt, @PathVariable String retweetedFrom, @PathVariable String retweetCreatedAt){
        restTemplate.postForEntity("http://localhost:8082/retweet/" +
                "createdAt/" +
                createdAt +
                "/username/" +
                username +
                "/retweetedFrom/" +
                retweetedFrom +
                "/retweetCreatedAt/" +
                retweetCreatedAt
                ,null, Object.class);
    }

    @PostMapping("/retweet/deleted/createdAt/{createdAt}/username/{username}/retweetedFrom/{retweetedFrom}")
    public void retweetDeleted(@PathVariable String username, @PathVariable String createdAt, @PathVariable String retweetedFrom){
        restTemplate.postForEntity("http://localhost:8082/retweet/deleted/" +
                "createdAt/" +
                createdAt +
                "/username/" +
                username +
                "/retweetedFrom/" +
                retweetedFrom,null, Object.class);
    }

    @PostMapping("/liked/createdAt/{createdAt}/username/{username}/originalOwnerUsername/{originalOwnerUsername}")
    public ResponseEntity<Object> liked(@PathVariable String username, @PathVariable String createdAt, @PathVariable String originalOwnerUsername){
        return restTemplate.postForEntity("http://localhost:8082/liked/" +
                "createdAt/" +
                createdAt +
                "/username/" +
                username +
                "/originalOwnerUsername/" +
                originalOwnerUsername,null, Object.class);
    }

    @PostMapping("/retweeted/createdAt/{createdAt}/username/{username}/originalOwnerUsername/{originalOwnerUsername}")
    public ResponseEntity<Object> retweeted(@PathVariable String username, @PathVariable String createdAt, @PathVariable String originalOwnerUsername){
        return restTemplate.postForEntity("http://localhost:8082/retweeted/" +
                "createdAt/" +
                createdAt +
                "/username/" +
                username +
                "/originalOwnerUsername/" +
                originalOwnerUsername,null, Object.class);
    }
}
