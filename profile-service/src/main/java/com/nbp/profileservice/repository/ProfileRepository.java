package com.nbp.profileservice.repository;

import com.nbp.profileservice.dto.cassandra.ProfileEntity;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface ProfileRepository extends CassandraRepository<ProfileEntity, String> {

    @Query("UPDATE profile " +
            "SET first_name =  " +
            ",last_name = ?1 " +
            ",image = ?2 " +
            "WHERE username = ?3 IF EXISTS")
    ProfileEntity updateProfile(final String first_name, final String last_name, final String image, final String username);

    ProfileEntity findByUsername(String username);
}
