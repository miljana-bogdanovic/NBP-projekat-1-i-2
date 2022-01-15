package com.nbp.tweetsservice.dto.redis;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class TweetMessage implements Serializable {

    private String tweetId;

    private String username;

    private String body;

    private String createdAt;

    private Integer likes;

    private String photo;

    private Integer retweets;

    private Boolean isRetweet;

    private String originalCreatorUsername;

    private String firstName;

    private String lastName;
}
