package com.nbp.timelineservice.model.cassandra;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.util.Date;

@AllArgsConstructor
@Data
@Builder
@PrimaryKeyClass
public class PrimaryKeyUserline {

    @PrimaryKeyColumn(name = "username", type = PrimaryKeyType.PARTITIONED)
    private String username;

    @PrimaryKeyColumn(name = "created_at")
    private Date createdAt;
}