package com.nbp.timelineservice.model.cassandra;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

@PrimaryKeyClass
@Builder
@Data
public class PrimaryKeyUserFriends {

    @PrimaryKeyColumn(name = "username", type = PrimaryKeyType.PARTITIONED)
    private String username;

    @PrimaryKeyColumn(name = "friend_username")
    private String friendUsername;
}
