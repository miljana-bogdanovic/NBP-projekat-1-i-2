package com.nbp.tweetsservice.dto;

import com.nbp.tweetsservice.dto.cassandra.PrimaryKeyTweetEntity;
import com.nbp.tweetsservice.dto.cassandra.TweetEntity;
import com.nbp.tweetsservice.dto.domain.Tweet;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class Mapper {

    public TweetEntity map(Tweet tweet) throws ParseException {
        Date parse = new SimpleDateFormat("YYYY-MM-dd hh:mm:ss").parse(tweet.getCreatedAt());
        TweetEntity tweetEntity = new TweetEntity();
        tweetEntity.setPrimaryKey(new PrimaryKeyTweetEntity(tweet.getUsername(), parse.toInstant().toEpochMilli()));
        tweetEntity.setLikes(tweet.getLikes());
        tweetEntity.setBody(tweet.getBody());
        tweetEntity.setCreatedAtTimestamp(tweet.getCreatedAt());
        tweetEntity.setRetweets(tweet.getRetweets());
        tweetEntity.setPhoto(tweet.getPhoto());
        tweetEntity.setIsRetweet(tweet.getIsRetweet());
        tweetEntity.setOriginalOwnerUsername(tweet.getOriginalOwnerUsername());
        tweetEntity.setFirstName(tweet.getFirstName());
        tweetEntity.setLastName(tweet.getLastName());
        tweetEntity.setRetweetedFrom(tweet.getRetweetedFrom());
        tweetEntity.setIsRetweetedByUser(tweet.getIsRetweetedByUser());
        return tweetEntity;
    }


    public Tweet map(TweetEntity tweetEntity) {
        Tweet tweet = new Tweet();
        tweet.setUsername(tweetEntity.getPrimaryKey().getUsername());
        tweet.setCreatedAt(tweetEntity.getCreatedAtTimestamp());
        tweet.setLikes(tweetEntity.getLikes());
        tweet.setBody(tweetEntity.getBody());
        tweet.setRetweets(tweetEntity.getRetweets());
        tweet.setPhoto(tweetEntity.getPhoto());
        tweet.setIsRetweet(tweetEntity.getIsRetweet());
        tweet.setOriginalOwnerUsername(tweetEntity.getOriginalOwnerUsername());
        tweet.setFirstName(tweetEntity.getFirstName());
        tweet.setLastName(tweetEntity.getLastName());
        tweet.setRetweetedFrom(tweetEntity.getRetweetedFrom());
        return tweet;
    }
}
