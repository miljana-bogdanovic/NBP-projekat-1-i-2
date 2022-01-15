package com.nbp.tweetsservice.repositories;

import com.nbp.tweetsservice.dto.cassandra.PrimaryKeyTweetEntity;
import com.nbp.tweetsservice.dto.cassandra.TweetEntity;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface TweetRepository extends CassandraRepository<TweetEntity, PrimaryKeyTweetEntity> {

    @Query("UPDATE mykeyspace.tweet " +
            "SET body = ?0" +
            "WHERE username = ?1 AND createdAt = ?2 IF EXISTS")
    TweetEntity updateTweet(final String body, final String username, final Long createdAt);

    List<TweetEntity> findByPrimaryKeyUsername(String username);

    TweetEntity findByPrimaryKeyUsernameAndPrimaryKeyCreatedAt(String username, Long createdAt);

    void deleteByPrimaryKeyUsername(String username);

    @Query(allowFiltering = true)
    List<TweetEntity> findByPrimaryKeyUsernameAndIsRetweetFalse(String username);
}
