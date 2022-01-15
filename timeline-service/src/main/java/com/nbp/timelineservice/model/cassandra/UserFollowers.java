package com.nbp.timelineservice.model.cassandra;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDateTime;

@Table("user_followers")
@Data
@Builder
public class UserFollowers {
    @PrimaryKey
    private PrimaryKeyUserFollowers primaryKey;

    @Column("followers_from")
    private LocalDateTime followersFrom;

    @Column("photo")
    private String photo;

    @Column("first_name")
    private String firstName;

    @Column("last_name")
    private String lastName;
}