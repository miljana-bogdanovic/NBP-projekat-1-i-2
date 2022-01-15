package com.nbp.timelineservice.model.domain;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserFollowersDto {
    private String username;

    private String followerUsername;

    private LocalDateTime followersFrom;

    private String photo;

    private String firstName;

    private String lastName;

}
