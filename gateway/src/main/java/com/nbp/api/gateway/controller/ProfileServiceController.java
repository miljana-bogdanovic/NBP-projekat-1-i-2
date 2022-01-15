package com.nbp.api.gateway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
public class ProfileServiceController {

    @Autowired
    RestTemplate restTemplate;

    @PostMapping(path = "/profile")
    public ResponseEntity<Object> createProfile(@RequestBody Object profile){
        return restTemplate.postForEntity("http://localhost:8083/profile", profile, Object.class);
    }

    @GetMapping(path = "/profile/{username}")
    public ResponseEntity<Object> getProfile(@PathVariable String username){
        return restTemplate.getForEntity("http://localhost:8083/profile/" + username, Object.class);
    }

    @GetMapping(path = "/users")
    public ResponseEntity<Object[]> getAllProfile(){
        return restTemplate.getForEntity("http://localhost:8083/users", Object[].class);
    }

    @PatchMapping(path = "/profile")
    public ResponseEntity<Object> updateProfile(@RequestBody Object profile){
        return ResponseEntity.ok(restTemplate.patchForObject("http://localhost:8083/profile", profile, Object.class));
    }

    @DeleteMapping(path = "/profile/{username}")
    public void deleteProfile(@PathVariable String username){
        restTemplate.delete("http://localhost:8083/profile/" + username);
    }
}
