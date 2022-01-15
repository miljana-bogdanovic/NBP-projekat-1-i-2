package com.nbp.timelineservice.config;

import com.nbp.timelineservice.repository.TimelineRepository;
import com.nbp.timelineservice.repository.UserFollowersRepository;
import com.nbp.timelineservice.repository.UserFriendsRepository;
import com.nbp.timelineservice.repository.UserlineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProfileDeletedMessageSubscriber implements MessageListener {

    public static List<String> messageList = new ArrayList<>();

    @Autowired
    private TimelineRepository timelineRepository;
    @Autowired
    private UserlineRepository userlineRepository;
    @Autowired
    private UserFollowersRepository userFollowersRepository;
    @Autowired
    private UserFriendsRepository userFriendsRepository;


    public void onMessage(final Message message, final byte[] pattern) {
        messageList.add(message.toString());
        System.out.println("Message received: " + new String(message.getBody()));

        timelineRepository.deleteTimeline(message.toString());
        userlineRepository.deleteUserline(message.toString());
        userFollowersRepository.deleteFollowersForUser(message.toString());
        userFriendsRepository.deleteFriendsForUser(message.toString());
    }
}
