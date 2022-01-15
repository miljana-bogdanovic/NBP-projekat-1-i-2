package com.nbp.tweetsservice.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nbp.tweetsservice.dto.Mapper;
import com.nbp.tweetsservice.dto.cassandra.PrimaryKeyTweetEntity;
import com.nbp.tweetsservice.dto.cassandra.TweetEntity;
import com.nbp.tweetsservice.dto.domain.Tweet;
import com.nbp.tweetsservice.messaging.impl.RedisTweetCreatedMessagePublisher;
import com.nbp.tweetsservice.messaging.impl.RedisTweetDeletedMessagePublisher;
import com.nbp.tweetsservice.messaging.impl.RedisTweetUpdatedMessagePublisher;
import com.nbp.tweetsservice.repositories.TweetRepository;
import com.nbp.tweetsservice.services.TweetService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TweetServiceImpl implements TweetService {

    @Autowired
    private TweetRepository tweetRepository;

    @Autowired
    private RedisTweetUpdatedMessagePublisher tweetUpdatedPublisher;

    @Autowired
    private RedisTweetCreatedMessagePublisher tweetCreatedPublisher;

    @Autowired
    private RedisTweetDeletedMessagePublisher tweetDeletedPublisher;

    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Mapper mapper;

    @Override
    public Tweet createTweet(Tweet tweet) throws JsonProcessingException, ParseException {
        TweetEntity persistedTweet = tweetRepository.save(mapper.map(tweet));
        //emit event -> TweetCreated
        tweetCreatedPublisher.publish(objectMapper.writeValueAsString(mapper.map(persistedTweet)));
        return mapper.map(persistedTweet);
    }

    @Override
    public Tweet getTweet(String username, String createdAt) throws ParseException {
        Date parse = new SimpleDateFormat("YYYY-MM-dd hh:mm:ss").parse(createdAt);
        TweetEntity foundTweet = tweetRepository.findByPrimaryKeyUsernameAndPrimaryKeyCreatedAt(username, parse.toInstant().toEpochMilli());
        return foundTweet != null ? mapper.map(foundTweet) : Tweet.builder().build();
    }

    @Override
    public Tweet updateTweetBody(Tweet tweet) throws JsonProcessingException, ParseException {
        Date parse = new SimpleDateFormat("YYYY-MM-dd hh:mm:ss").parse(tweet.getCreatedAt());
        Optional<TweetEntity> entity = tweetRepository.findById(PrimaryKeyTweetEntity
                .builder()
                .username(tweet.getUsername())
                .createdAt(parse.toInstant().toEpochMilli())
                .build());

        if (entity.isPresent()) {
            TweetEntity t = entity.get();
            if(tweet.getBody() != null)
                t.setBody(tweet.getBody());
            //if(tweet.getLikes() != null)
             //   t.setLikes(tweet.getLikes());
            //if(tweet.getRetweets() != null)
            //    t.setRetweets(tweet.getRetweets());
            tweetRepository.save(t);
            tweetUpdatedPublisher.publish(objectMapper.writeValueAsString(mapper.map(t)));
        }


        return tweet;
    }

    @Override
    public void deleteTweet(String username, String createdAt, String retweetedFrom) throws JsonProcessingException, ParseException {
        Date parse = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(createdAt);
        PrimaryKeyTweetEntity pk = new PrimaryKeyTweetEntity(username, parse.toInstant().toEpochMilli());
        tweetRepository.deleteById(pk);
        //emit event -> TweetDeleted
        tweetDeletedPublisher.publish(objectMapper
                .writeValueAsString(Tweet.builder().username(username).createdAt(createdAt).retweetedFrom(retweetedFrom).build()));
    }

    @Override
    public List<Tweet> getTweetAllTweetsForUser(String username) {
        return tweetRepository.findByPrimaryKeyUsername(username).stream().map(mapper::map).collect(Collectors.toList());
    }



//    @EventListener(ApplicationReadyEvent.class)
//    public void test() throws JsonProcessingException {
//        tweetPublisher.publish(objectMapper.writeValueAsString(new TweetMessage("a", "a", "a", "a")));
//        tweetUpdatedPublisher.publish(objectMapper.writeValueAsString(new TweetMessage("a", "a", "a", "a")));
//    }
}
