package com.nbp.timelineservice.service;


import com.nbp.timelineservice.model.cassandra.Timeline;
import com.nbp.timelineservice.model.domain.TimelineDto;
import com.nbp.timelineservice.model.domain.UserlineDto;

import java.sql.Time;
import java.util.List;

public interface TimelineService {
    List<TimelineDto> getTimelineForUser(String username);

    void save(Timeline timeline);

    void addTweetsToTimeline(String username, String followedUsername);

    void removeTweetsFromTimeline(String username, String followedUsername);

    List<TimelineDto> getAllTweets(String username);
}
