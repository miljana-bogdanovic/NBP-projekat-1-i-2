package com.nbp.timelineservice.repository;

import com.nbp.timelineservice.model.cassandra.PrimaryKeyUserline;
import com.nbp.timelineservice.model.cassandra.Timeline;
import com.nbp.timelineservice.model.cassandra.Userline;
import com.nbp.timelineservice.model.domain.UserlineDto;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

import java.util.Date;
import java.util.List;

public interface UserlineRepository extends CassandraRepository<Userline, PrimaryKeyUserline> {

    @Query("SELECT * FROM userline WHERE username = ?0")
    List<Userline> findByPartitionKey(String username);

    @Query("SELECT * FROM userline WHERE username = ?0 AND created_at = ?1")
    Userline findByPk(String username, Date createdAt);

    @Query("DELETE FROM userline WHERE username = ?0")
    void deleteUserline(String username);

    @Query("SELECT * FROM userline WHERE username = ?0 AND created_at = ?1")
    List<Userline> findByPartitionKeyAndCreatedAt(String username, Date parse);

    @Query("SELECT * FROM userline ")
    List<Userline> getAllTweets();
}
