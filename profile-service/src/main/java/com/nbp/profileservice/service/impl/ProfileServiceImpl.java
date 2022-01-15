package com.nbp.profileservice.service.impl;

import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.update.UpdateStart;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nbp.profileservice.dto.cassandra.ProfileEntity;
import com.nbp.profileservice.dto.domain.Profile;
import com.nbp.profileservice.messaging.impl.RedisProfileCreatedMessagePublisher;
import com.nbp.profileservice.messaging.impl.RedisProfileDeletedMessagePublisher;
import com.nbp.profileservice.messaging.impl.RedisProfileUpdatedMessagePublisher;
import com.nbp.profileservice.repository.ProfileRepository;
import com.nbp.profileservice.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProfileServiceImpl implements ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private RedisProfileUpdatedMessagePublisher profileUpdatedPublisher;

    @Autowired
    private RedisProfileCreatedMessagePublisher profileCreatedPublisher;

    @Autowired
    private RedisProfileDeletedMessagePublisher profileDeletedPublisher;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public Profile createProfile(Profile profile) throws JsonProcessingException {
        profile.setPassword(encoder.encode(profile.getPassword()));
        ProfileEntity persistedProfile = profileRepository.save(objectMapper.convertValue(profile, ProfileEntity.class));
        //emit event -> TweetCreated
        profileCreatedPublisher.publish(objectMapper.writeValueAsString(objectMapper.convertValue(persistedProfile, Profile.class)));
        return objectMapper.convertValue(persistedProfile, Profile.class);
    }

    @Override
    public Profile getProfile(String username) {
        ProfileEntity foundProfile = profileRepository.findByUsername(username);
        return foundProfile != null ? objectMapper.convertValue(foundProfile,  Profile.class) : Profile.builder().build();
    }


    @Override
    public Profile updateProfileBody(Profile profile) throws JsonProcessingException {
        ProfileEntity entity = objectMapper.convertValue(profile, ProfileEntity.class);
        profileRepository.save(entity);
        //emit event -> TweetUpdated
        profileUpdatedPublisher.publish(objectMapper.writeValueAsString(profile));
        return profile;
    }

    @Override
    public void deleteProfile(String username) throws JsonProcessingException {
        profileRepository.deleteById(username);
        //emit event -> TweetDeleted
        profileDeletedPublisher.publish(username);
    }

    @Override
    public List<Profile> getAllProfiles() {
        return profileRepository.findAll().stream().map(t -> objectMapper.convertValue(t, Profile.class)).collect(Collectors.toList());
    }
}
