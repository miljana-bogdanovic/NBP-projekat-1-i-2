package com.nbp.timelineservice.model.cassandra.likes;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.util.Date;

@PrimaryKeyClass
@Data
@Builder
public class PrimaryKeyLiked {
    @PrimaryKeyColumn(name = "username", type = PrimaryKeyType.PARTITIONED)
    private String username;

    @PrimaryKeyColumn(name = "created_at")
    private Date createdAt;

    @PrimaryKeyColumn("username_who_likes_the_tweet")
    private String usernameWhoLikesTheTweet;
}
