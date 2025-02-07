package com.team3.devinit_back.global.config;

import com.team3.devinit_back.websocket.dto.ChatMessageDto;
import com.team3.devinit_back.websocket.entity.ChatMessage;
import com.team3.devinit_back.websocket.service.RedisSubscriber;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Bean
    @Primary
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setEnableTransactionSupport(true);
        return template;
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(redisHost);
        redisStandaloneConfiguration.setPort(redisPort);
        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean
    @Qualifier("chatRedisTemplate")
    public RedisTemplate<String, ChatMessageDto> chatRedisTemplate() {
        RedisTemplate<String, ChatMessageDto> chatRedisTemplate = new RedisTemplate<>();
        chatRedisTemplate.setConnectionFactory(redisConnectionFactory());
        chatRedisTemplate.setKeySerializer(new StringRedisSerializer());
        chatRedisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(ChatMessageDto.class));
        return chatRedisTemplate;
    }

    @Bean
    @Qualifier("chatMessageRedisTemplate")
    public RedisTemplate<String, ChatMessage> chatMessageRedisTemplate() {
        RedisTemplate<String, ChatMessage> chatMessageRedisTemplate = new RedisTemplate<>();
        chatMessageRedisTemplate.setConnectionFactory(redisConnectionFactory());
        chatMessageRedisTemplate.setKeySerializer(new StringRedisSerializer());
        chatMessageRedisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(ChatMessage.class));
        return chatMessageRedisTemplate;
    }

    @Bean
    public MessageListenerAdapter messageListenerAdapter(RedisSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber, "sendMessage");
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
        RedisConnectionFactory connectionFactory,
        MessageListenerAdapter listenerAdapter,
        ChannelTopic channelTopic) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listenerAdapter, channelTopic);
        return container;
    }

    @Bean
    public ChannelTopic topic() {
        return new ChannelTopic("chat");
    }
}