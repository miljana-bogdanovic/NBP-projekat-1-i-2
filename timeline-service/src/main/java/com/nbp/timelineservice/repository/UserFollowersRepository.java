package com.nbp.timelineservice.repository;

import com.nbp.timelineservice.model.cassandra.PrimaryKeyUserFollowers;
import com.nbp.timelineservice.model.cassandra.UserFollowers;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserFollowersRepository extends CassandraRepository<UserFollowers, PrimaryKeyUserFollowers> {

    @Query("SELECT * FROM user_followers WHERE username = ?0")
    List<UserFollowers> findByPartitionKey(String username);

    @Query("DELETE FROM user_followers WHERE followerUsername = ?0")
    void deleteByUserId(String id);

    @Query("DELETE FROM user_followers WHERE username = ?0 AND follower_username = ?1")
    void deleteFollowerForUser(String username, String followedUsername);

    @Query("DELETE FROM user_followers WHERE username = ?0")
    void deleteFollowersForUser(String toString);

    @Query(allowFiltering = true)
    List<UserFollowers> findByPrimaryKeyFollowerUsername(String username);
}
