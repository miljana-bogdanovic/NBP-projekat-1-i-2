package com.nbp.timelineservice.repository;

import com.nbp.timelineservice.model.cassandra.PrimaryKeyTimeline;
import com.nbp.timelineservice.model.cassandra.Timeline;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TimelineRepository extends CassandraRepository<Timeline, PrimaryKeyTimeline> {

    @Query("SELECT * FROM timeline WHERE username = ?0")
    List<Timeline> findByPartitionKey(String username);

    @Query("SELECT * FROM timeline WHERE username = ?0 AND created_at = ?1")
    List<Timeline> findByPartitionKeyAndCreatedAt(String username, Date createdAt);

    @Query("DELETE FROM timeline WHERE username = ?0")
    void deleteTimeline(String username);

    @Query("DELETE FROM timeline WHERE username in ?0 AND originalOwnerUsername = ?1 AND created_at = ?2")
    void deleteTweet(List<String> pks, String username, Date createdAt);

    @Query("SELECT * FROM timeline ")
    List<Timeline> getAllTweets();
}


