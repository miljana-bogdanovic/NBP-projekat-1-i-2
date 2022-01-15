package com.nbp.timelineservice.model.domain;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserlineDto implements Serializable {

    private String username;

    private Date createdAt;

    private String originalOwnerUsername;

    private String firstName;

    private String lastName;

    private Integer likes;

    private String body;

    private String photo;

    private Integer retweets;

    private Boolean isRetweet;

    private Boolean isLikedByUser;

    private String retweetedFrom;

    private Boolean isRetweetedByUser;
}
