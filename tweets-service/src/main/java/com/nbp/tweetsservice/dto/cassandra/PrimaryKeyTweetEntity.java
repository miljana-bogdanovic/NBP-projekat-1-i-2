package com.nbp.tweetsservice.dto.cassandra;

import lombok.Builder;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.io.Serializable;

@Builder
@PrimaryKeyClass
public class PrimaryKeyTweetEntity implements Serializable {
    @PrimaryKeyColumn(name = "username", type = PrimaryKeyType.PARTITIONED)
    private String username;

    @PrimaryKeyColumn(name = "createdAt")
    private Long createdAt;

    public PrimaryKeyTweetEntity(){}

    public PrimaryKeyTweetEntity(String username, Long createdAt) {
        this.username = username;
        this.createdAt = createdAt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }
}

