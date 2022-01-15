package com.nbp.tweetsservice.dto.cassandra;

import lombok.Data;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;


@Table("tweet")
@Data
public class TweetEntity {

    @PrimaryKey
    private PrimaryKeyTweetEntity primaryKey;

    @Column("likes")
    private Integer likes;

    @Column("body")
    private String body;

    @Column("createdAtTimestamp")
    private String createdAtTimestamp;

    @Column("first_name")
    private String firstName;

    @Column("last_name")
    private String lastName;

    @Column("photo")
    private String photo;

    @Column("isRetweet")
    private Boolean isRetweet;

    @Column("retweets")
    private Integer retweets;

    @Column("originalCreatorUsername")
    private String originalOwnerUsername;

    @Column("retweetedFrom")
    private String retweetedFrom;

    @Column("isRetweetedByUser")
    private Boolean isRetweetedByUser;
}
