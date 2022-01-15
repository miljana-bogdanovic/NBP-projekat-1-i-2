package com.nbp.timelineservice.model.cassandra.retweets;

import lombok.*;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.Date;

@Table("retweeted")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Retweeted {
    @PrimaryKey
    private PrimaryKeyRetweeted primaryKeyRetweeted;
    @Column("retweetCreatedAt")
    private Date retweetCreatedAt;
}
