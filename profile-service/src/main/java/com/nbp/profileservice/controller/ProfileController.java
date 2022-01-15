package com.nbp.profileservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nbp.profileservice.service.ProfileService;
import com.nbp.profileservice.dto.domain.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @PostMapping(path = "/profile")
    public ResponseEntity<Profile> createProfile(@RequestBody Profile profile) throws JsonProcessingException {
        return ResponseEntity.ok(profileService.createProfile(profile));
    }

    @GetMapping(path = "/profile/{username}")
    public ResponseEntity<Profile> getProfile(@PathVariable String username) {
        return ResponseEntity.ok(profileService.getProfile(username));
    }

    @GetMapping(path = "/users")
    public ResponseEntity<List<Profile>> getAllProfiles() {
        return ResponseEntity.ok(profileService.getAllProfiles());
    }

    @PatchMapping(path = "/profile")
    public ResponseEntity<Profile> updateProfile(@RequestBody Profile profile) throws JsonProcessingException {
        return ResponseEntity.ok(profileService.updateProfileBody(profile));
    }

    @DeleteMapping(path = "/profile/{username}")
    public ResponseEntity<Void> deleteProfile(@PathVariable String username) throws JsonProcessingException {
        profileService.deleteProfile(username);
        return ResponseEntity.ok(null);
    }
}
