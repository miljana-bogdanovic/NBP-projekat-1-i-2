package com.nbp.api.gateway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
public class TweetServiceController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping(path = "/tweet/username/{username}/createdAt/{createdAt}")
    ResponseEntity<Object> getTweet(@PathVariable String username, @PathVariable String createdAt) {
        return restTemplate.getForEntity("http://localhost:8081/username/" +
                username +
                "/" +
                "createdAt/" +
                createdAt, Object.class);
    }

    @PostMapping(path = "/tweet")
    public ResponseEntity<Object> createTweet(@RequestBody Object tweet) {
        return restTemplate.postForEntity("http://localhost:8081/tweet",tweet, Object.class);
    }

    @GetMapping(path = "/tweet/username/{username}")
    public ResponseEntity<Object[]> getTweets(@PathVariable String username){
        return restTemplate.getForEntity("http://localhost:8081/username/" + username, Object[].class);
    }



    @PatchMapping(path = "/tweet")
    public ResponseEntity<Object> updateTweets(@RequestBody Object updatedTweet){
        return ResponseEntity.ok(restTemplate.patchForObject("http://localhost:8081/tweet", updatedTweet, Object.class));
    }

    @DeleteMapping(path = "/tweet/username/{username}/createdAt/{createdAt}/retweetedFrom/{retweetedFrom}")
    public void deleteTweet(@PathVariable String username, @PathVariable String createdAt, @PathVariable String retweetedFrom ){
        restTemplate.delete("http://localhost:8081/username/" +
                username +
                "/" +
                "createdAt/" +
                createdAt+
                "/retweetedFrom/"+
                retweetedFrom);
    }



}
