package com.nbp.tweetsservice.dto.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tweet {
    private String username;

    private String body;

    private String createdAt;

    private Integer likes;

    private String photo;

    private Integer retweets;

    private Boolean isRetweet;

    private String originalOwnerUsername;

    private String firstName;

    private String lastName;

    private String retweetedFrom;

    private Boolean isRetweetedByUser;
}
