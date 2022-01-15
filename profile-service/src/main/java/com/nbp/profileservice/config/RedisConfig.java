package com.nbp.profileservice.config;

import com.nbp.profileservice.messaging.RedisMessageSubscriber;
import com.nbp.profileservice.messaging.RedisMessageSubscriberUnfollow;
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
@ComponentScan("com.nbp.tweetsservice.messaging")
//@EnableRedisRepositories(basePackages = "com.baeldung.spring.data.redis.repo")
@PropertySource("classpath:application.properties")
public class RedisConfig {

    @Autowired
    private RedisMessageSubscriber redisMessageSubscriber;

    @Autowired
    private RedisMessageSubscriberUnfollow unfollowSubscriber;

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
        return new MessageListenerAdapter(redisMessageSubscriber);
    }

    @Bean
    MessageListenerAdapter messageListenerUnfollow() {
        return new MessageListenerAdapter(unfollowSubscriber);
    }

    @Bean
    RedisMessageListenerContainer redisContainer() {
        final RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(messageListener(), profileFollowedTopic());
        container.addMessageListener(messageListenerUnfollow(), profileUnfollowedTopic());
        return container;
    }

    @Bean
    ChannelTopic profileFollowedTopic(){
        return new ChannelTopic("profile:followed");
    }

    @Bean
    ChannelTopic profileUnfollowedTopic(){
        return new ChannelTopic("profile:unfollowed");
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
    ChannelTopic profileDeletedTopic() {
        return new ChannelTopic("profile:deleted");
    }
}
