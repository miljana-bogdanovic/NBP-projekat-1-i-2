package com.nbp.tweetsservice.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nbp.tweetsservice.dto.domain.Tweet;
import com.nbp.tweetsservice.services.TweetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;


@RestController
public class TweetController {

    @Autowired
    private TweetService tweetService;

    @PostMapping(path = "/tweet")
    public ResponseEntity<Tweet> createTweet(@RequestBody Tweet tweet) throws JsonProcessingException, ParseException {
        return ResponseEntity.ok(tweetService.createTweet(tweet));
    }

    @GetMapping(path = "/username/{username}/createdAt/{createdAt}")
    public ResponseEntity<Tweet> getTweet(@PathVariable String username, @PathVariable String createdAt) throws ParseException {
        return ResponseEntity.ok(tweetService.getTweet(username, createdAt));
    }

    @GetMapping(path = "/username/{username}")
    public ResponseEntity<List<Tweet>> getTweets(@PathVariable String username){
        return ResponseEntity.ok(tweetService.getTweetAllTweetsForUser(username));
    }

    @PatchMapping(path = "/tweet")
    public ResponseEntity<Tweet> updateTweet(@RequestBody Tweet tweet) throws JsonProcessingException, ParseException {
        return ResponseEntity.ok(tweetService.updateTweetBody(tweet));
    }

    @DeleteMapping(path = "/username/{username}/createdAt/{createdAt}/retweetedFrom/{retweetedFrom}")
    public ResponseEntity<Void> deleteTweet(@PathVariable String username, @PathVariable String createdAt, @PathVariable String retweetedFrom) throws JsonProcessingException, ParseException {
        tweetService.deleteTweet(username, createdAt, retweetedFrom);
        return ResponseEntity.ok(null);
    }
}
