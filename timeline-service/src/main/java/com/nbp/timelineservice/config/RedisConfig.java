package com.nbp.timelineservice.config;

import com.nbp.timelineservice.service.RedisLockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericToStringSerializer;

@Configuration
@ComponentScan("com.nbp.timelineservice.messaging")
@PropertySource("classpath:application.properties")
public class RedisConfig {

    @Autowired
    private TweetCreatedMessageSubscriber tweetCreatedMessageSubscriber;
    @Autowired
    private TweetUpdatedMessageSubscriber tweetUpdatedMessageSubscriber;
    @Autowired
    private TweetDeletedMessageSubscriber tweetDeletedMessageSubscriber;

    @Autowired
    private ProfileCreatedMessageSubscriber profileCreatedMessageSubscriber;
    @Autowired
    private ProfileUpdatedMessageSubscriber profileUpdatedMessageSubscriber;
    @Autowired
    private ProfileDeletedMessageSubscriber profileDeletedMessageSubscriber;

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory();
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        final RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setValueSerializer(new GenericToStringSerializer<Object>(Object.class));
        return template;
    }

    @Bean
    MessageListenerAdapter messageListener() {
        return new MessageListenerAdapter(tweetCreatedMessageSubscriber);
    }

    @Bean
    MessageListenerAdapter messageListenerUpdatedTweet() {
        return new MessageListenerAdapter(tweetUpdatedMessageSubscriber);
    }

    @Bean
    MessageListenerAdapter messageListenerDeletedTweet() {
        return new MessageListenerAdapter(tweetDeletedMessageSubscriber);
    }


    @Bean
    MessageListenerAdapter profileCreatedMessageListener() {
        return new MessageListenerAdapter(profileCreatedMessageSubscriber);
    }

    @Bean
    MessageListenerAdapter profileUpdatedMessageListener() {
        return new MessageListenerAdapter(profileUpdatedMessageSubscriber);
    }

    @Bean
    MessageListenerAdapter profileDeletedMessageListener() {
        return new MessageListenerAdapter(profileDeletedMessageSubscriber);
    }

    @Bean
    RedisMessageListenerContainer redisContainer() {
        final RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());

        container.addMessageListener(messageListener(), topicTweetCreated());
        container.addMessageListener(messageListenerUpdatedTweet(), topicTweetUpdated());
        container.addMessageListener(messageListenerDeletedTweet(), topicTweetDeleted());

        container.addMessageListener(profileCreatedMessageListener(), profileCreatedTopic());
        container.addMessageListener(profileUpdatedMessageListener(), profileUpdatedTopic());
        container.addMessageListener(profileDeletedMessageListener(), profileDeletedTopic());
        return container;
    }

    @Bean
    ChannelTopic topicTweetCreated() {
        return new ChannelTopic("tweet:created");
    }

    @Bean
    ChannelTopic topicTweetUpdated() {
        return new ChannelTopic("tweet:updated");
    }

    @Bean
    ChannelTopic topicTweetDeleted() {
        return new ChannelTopic("tweet:deleted");
    }

    @Bean
    ChannelTopic profileCreatedTopic() {
        return new ChannelTopic("profile:created");
    }

    @Bean
    ChannelTopic profileUpdatedTopic() {
        return new ChannelTopic("profile:updated");
    }

    @Bean
    ChannelTopic profileDeletedTopic() { return new ChannelTopic("profile:deleted"); }

    @Bean
    ChannelTopic profileFollowedTopic(){
        return new ChannelTopic("profile:followed");
    }

    @Bean
    ChannelTopic profileUnfollowedTopic(){
        return new ChannelTopic("profile:unfollowed");
    }
}
