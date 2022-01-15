package com.nbp.tweetsservice.config;

import com.nbp.tweetsservice.messaging.impl.ProfileUpdatedMessageSubscriber;
import com.nbp.tweetsservice.messaging.impl.RedisMessageSubscriber;
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
    private ProfileUpdatedMessageSubscriber profileUpdatedMessageSubscriber;

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
    MessageListenerAdapter messageListenerProfileUpdated() {
        return new MessageListenerAdapter(profileUpdatedMessageSubscriber);
    }

    @Bean
    RedisMessageListenerContainer redisContainer() {
        final RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(messageListener(), profileDeletedTopic());
        container.addMessageListener(messageListenerProfileUpdated(), profileUpdatedTopic());
        return container;
    }

    @Bean
    ChannelTopic tweetCreatedTopic() {
        return new ChannelTopic("tweet:created");
    }

    @Bean
    ChannelTopic profileDeletedTopic() {
        return new ChannelTopic("profile:deleted");
    }


    @Bean
    ChannelTopic profileUpdatedTopic() {
        return new ChannelTopic("profile:updated");
    }

    @Bean
    ChannelTopic tweetUpdatedTopic() {
        return new ChannelTopic("tweet:updated");
    }

    @Bean
    ChannelTopic tweetDeletedTopic() {
        return new ChannelTopic("tweet:deleted");
    }
}
