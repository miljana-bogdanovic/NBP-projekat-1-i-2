package com.nbp.timelineservice.service.impl;

import com.nbp.timelineservice.model.cassandra.PrimaryKeyUserFollowers;
import com.nbp.timelineservice.model.cassandra.UserFollowers;
import com.nbp.timelineservice.model.domain.UserFollowersDto;
import com.nbp.timelineservice.repository.UserFollowersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserFollowersService {

    @Autowired
    private UserFollowersRepository userFollowersRepository;

    public List<UserFollowers> getFollowersForUser(String username) {
       return userFollowersRepository.findByPartitionKey(username);
    }

    public void deleteFollowersForUser(String id) {
        userFollowersRepository.deleteByUserId(id);
    }

    public void addFollowerForUser(String username, String follows) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<UserFriendsService.Profile> forEntity = restTemplate.getForEntity("http://localhost:8083/profile/" + username, UserFriendsService.Profile.class);
        UserFriendsService.Profile profile = forEntity.getBody();

        userFollowersRepository.insert(UserFollowers
                .builder()
                .primaryKey(PrimaryKeyUserFollowers
                        .builder()
                        .username(follows)
                        .followerUsername(username).build())
                .followersFrom(LocalDateTime.now())
                .firstName(profile.getFirstName())
                .lastName(profile.getLastName())
                .photo(profile.getImage())
                .build()
        );
    }

    public void removeFollowerForUser(String username, String unfollowed) {
        userFollowersRepository.deleteFollowerForUser(unfollowed, username);
    }

    public List<UserFollowersDto> getFollowersForUserDto(String username) {
        List<UserFollowers> byPartitionKey = userFollowersRepository.findByPartitionKey(username);
        List<UserFollowersDto> dtos = new ArrayList<>();
        for(UserFollowers t : byPartitionKey){
            UserFollowersDto build = UserFollowersDto
                    .builder()
                    .followersFrom(t.getFollowersFrom())
                    .lastName(t.getLastName())
                    .firstName(t.getFirstName())
                    .photo(t.getPhoto())
                    .username(t.getPrimaryKey().getUsername())
                    .followerUsername(t.getPrimaryKey().getFollowerUsername())
                    .build();
            dtos.add(build);
        }
        return dtos;
    }
}
