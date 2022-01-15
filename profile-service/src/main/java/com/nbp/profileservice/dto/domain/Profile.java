package com.nbp.profileservice.dto.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Profile {

    private String username;

    private Integer followers;

    private String following;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private String image;

}
