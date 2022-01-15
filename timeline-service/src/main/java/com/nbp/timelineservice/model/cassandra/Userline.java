package com.nbp.timelineservice.model.cassandra;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("userline")
@Data
@Getter
@Setter
public class Userline {
    @PrimaryKey
    private PrimaryKeyUserline primaryKey;

    @Column("first_name")
    private String firstName;

    @Column("last_name")
    private String lastName;

    @Column("body")
    private String body;

    @Column("photo")
    private String photo;

    @Column("retweets")
    private Integer retweets;

    @Column("isRetweet")
    private Boolean isRetweet;

    @Column("originalCreatorUsername")
    private String originalOwnerUsername;

    @Column("likes")
    private Integer likes;

    @Column("isLikedByUser")
    private Boolean isLikedByUser;

    @Column("isRetweetedByUser")
    private Boolean isRetweetedByUser;

    @Column("retweetedFrom")
    private String retweetedFrom;
}
