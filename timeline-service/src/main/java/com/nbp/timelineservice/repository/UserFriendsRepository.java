package com.nbp.timelineservice.repository;

import com.nbp.timelineservice.model.cassandra.PrimaryKeyUserFollowers;
import com.nbp.timelineservice.model.cassandra.PrimaryKeyUserFriends;
import com.nbp.timelineservice.model.cassandra.UserFollowers;
import com.nbp.timelineservice.model.cassandra.UserFriends;
import com.nbp.timelineservice.service.impl.UserFriendsService;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserFriendsRepository extends CassandraRepository<UserFriends, PrimaryKeyUserFriends> {

    @Query("SELECT * FROM user_friends WHERE username = ?0")
    List<UserFriends> findByPartitionKey(String username);

    @Query("DELETE FROM user_friends WHERE followerUsername = ?0")
    void deleteByUserId(String id);

    @Query("DELETE FROM user_friends WHERE username = ?0 AND friend_username = ?1")
    void deleteFriendForUser(String username, String followedUsername);

    @Query("DELETE FROM user_friends WHERE username = ?0")
    void deleteFriendsForUser(String toString);

    @Query(allowFiltering = true)
    List<UserFriends> findByPrimaryKeyFriendUsername(String username);
}
