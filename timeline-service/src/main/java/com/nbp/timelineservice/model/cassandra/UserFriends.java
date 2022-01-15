package com.nbp.timelineservice.model.cassandra;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDateTime;

@Table("user_friends")
@Data
@Builder
public class UserFriends {
    @PrimaryKey
    private PrimaryKeyUserFriends primaryKey;

    @Column("friends_from")
    private LocalDateTime friendsFrom;

    @Column("photo")
    private String photo;

    @Column("first_name")
    private String firstName;

    @Column("last_name")
    private String lastName;
}
