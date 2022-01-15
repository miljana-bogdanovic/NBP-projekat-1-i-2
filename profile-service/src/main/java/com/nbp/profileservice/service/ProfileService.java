package com.nbp.profileservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nbp.profileservice.dto.domain.Profile;

import java.text.ParseException;
import java.util.List;

public interface ProfileService {
    Profile createProfile(Profile profile) throws JsonProcessingException;

    Profile getProfile(String username);

    Profile updateProfileBody(Profile tweet) throws JsonProcessingException;

    void deleteProfile(String username) throws JsonProcessingException;

    List<Profile> getAllProfiles();
}
