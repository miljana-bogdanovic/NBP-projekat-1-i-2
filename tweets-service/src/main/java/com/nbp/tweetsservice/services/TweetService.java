package com.nbp.tweetsservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nbp.tweetsservice.dto.domain.Tweet;

import java.text.ParseException;
import java.util.List;

public interface TweetService {
    Tweet createTweet(Tweet tweet) throws JsonProcessingException, ParseException;

    Tweet getTweet(String username, String createdAt) throws ParseException;

    Tweet updateTweetBody(Tweet tweet) throws JsonProcessingException, ParseException;

    void deleteTweet(String username, String createdAt, String retweetedFrom) throws JsonProcessingException, ParseException;

    List<Tweet> getTweetAllTweetsForUser(String username);

}
