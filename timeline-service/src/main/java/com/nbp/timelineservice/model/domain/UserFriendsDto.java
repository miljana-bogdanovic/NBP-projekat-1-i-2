package com.nbp.timelineservice.model.domain;

import com.nbp.timelineservice.model.cassandra.PrimaryKeyUserFriends;
import lombok.*;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserFriendsDto {
    private String username;

    private String friendUsername;

    private LocalDateTime friendsFrom;

    private String photo;

    private String firstName;

    private String lastName;
}
