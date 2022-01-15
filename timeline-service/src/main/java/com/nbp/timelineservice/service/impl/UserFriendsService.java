package com.nbp.timelineservice.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nbp.timelineservice.model.cassandra.PrimaryKeyUserFriends;
import com.nbp.timelineservice.model.cassandra.UserFollowers;
import com.nbp.timelineservice.model.cassandra.UserFriends;
import com.nbp.timelineservice.model.domain.UserFriendsDto;
import com.nbp.timelineservice.repository.UserFollowersRepository;
import com.nbp.timelineservice.repository.UserFriendsRepository;
import lombok.*;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserFriendsService {

    @Autowired
    ObjectMapper mapper;

    @Autowired
    private UserFriendsRepository userFriendsRepository;

    public List<UserFriends> getFollowersForUser(String username) {
       return userFriendsRepository.findByPartitionKey(username);
    }

    public List<UserFriendsDto> getFollowersForUserDto(String username) {
        List<UserFriends> byPartitionKey = userFriendsRepository.findByPartitionKey(username);
        List<UserFriendsDto> dtos = new ArrayList<>();
         for(UserFriends t : byPartitionKey){
             UserFriendsDto build = UserFriendsDto
                     .builder()
                     .friendsFrom(t.getFriendsFrom())
                     .lastName(t.getLastName())
                     .firstName(t.getFirstName())
                     .photo(t.getPhoto())
                     .username(t.getPrimaryKey().getUsername())
                     .friendUsername(t.getPrimaryKey().getFriendUsername())
                     .build();
             dtos.add(build);
         }
         return dtos;
    }

    public void deleteFriendsForUser(String id) {userFriendsRepository.deleteByUserId(id);}

    @SneakyThrows
    public void addFriendForUser(String username, String follows) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Profile> forEntity = restTemplate.getForEntity("http://localhost:8083/profile/" + follows, Profile.class);
        Profile profile = forEntity.getBody();

        userFriendsRepository
                .insert(
                        UserFriends
                                .builder()
                                .primaryKey(PrimaryKeyUserFriends
                                        .builder()
                                        .username(username)
                                        .friendUsername(follows)
                                        .build())
                                .friendsFrom(OffsetDateTime.now().toLocalDateTime())
                                .firstName(profile.firstName)
                                .lastName(profile.lastName)
                                .photo(profile.image)
                                .build()
                );
    }

    public void removeFriendForUser(String username, String followedUsername) {
        userFriendsRepository.deleteFriendForUser(username, followedUsername);
    }
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Profile {

        private String username;

        private Integer followers;

        private String following;

        private String firstName;

        private String lastName;

        private String email;

        private String password;

        private String image;

    }
}
