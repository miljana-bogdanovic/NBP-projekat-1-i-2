package com.nbp.timelineservice.model.cassandra;

import lombok.*;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("timeline")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Timeline {
    @PrimaryKey
    private PrimaryKeyTimeline primaryKey;

    @Column("first_name")
    private String firstName;

    @Column("last_name")
    private String lastName;

    @Column("likes")
    private Integer likes;

    @Column("body")
    private String body;

    @Column("photo")
    private String photo;

    @Column("retweets")
    private Integer retweets;

    @Column("isRetweet")
    private Boolean isRetweet;

    @Column("retweetedFrom")
    private String retweetedFrom;

    @Column("isLikedByUser")
    private Boolean isLikedByUser;

    @Column("isRetweetedByUser")
    private Boolean isRetweetedByUser;
}
