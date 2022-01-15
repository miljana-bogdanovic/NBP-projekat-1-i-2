package com.nbp.timelineservice.messaging;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class TweetCreated {
    private String username;

    private String body;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    private Integer likes;

    private String photo;

    private Integer retweets;

    private Boolean isRetweet;

    private Boolean isRetweetedByUser;

    private String originalOwnerUsername;

    private String firstName;

    private String lastName;

    private String retweetedFrom;
}
