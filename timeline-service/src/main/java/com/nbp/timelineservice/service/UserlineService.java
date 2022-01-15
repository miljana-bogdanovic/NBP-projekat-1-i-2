package com.nbp.timelineservice.service;


import com.nbp.timelineservice.model.cassandra.Userline;
import com.nbp.timelineservice.model.domain.UserlineDto;

import java.util.List;

public interface UserlineService {
    List<Userline> getUserline(String username);

    List<UserlineDto> getUserlineDto(String username);

    List<UserlineDto> getAllTweets(String username);

    void save(Userline userline);

    void delete(String id);

}
