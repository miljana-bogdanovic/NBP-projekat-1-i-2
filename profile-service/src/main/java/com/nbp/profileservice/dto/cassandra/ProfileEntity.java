package com.nbp.profileservice.dto.cassandra;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;


@Table("profile")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileEntity {

    @PrimaryKey
    private String username;

    @Column("followers")
    private Integer followers;

    //TODO:CHANGE TO INTEGER
    @Column("following")
    private String following;

    @Column("first_name")
    private String firstName;

    @Column("last_name")
    private String lastName;

    @Column("email")
    private String email;

    @Column("password")
    private String password;

    @Column("image")
    private String image;

}
