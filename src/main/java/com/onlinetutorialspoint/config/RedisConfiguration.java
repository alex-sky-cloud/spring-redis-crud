package com.onlinetutorialspoint.config;

import com.onlinetutorialspoint.model.Item;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;

@Configuration
public class RedisConfiguration {

    @Value("${server.redis.host}")
    private String hostName;

    @Value("${server.redis.port}")
    private int port;


    @Bean
    JedisConnectionFactory jedisConnectionFactory(){

        RedisStandaloneConfiguration redisStandaloneConfiguration =
                new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(this.hostName);
        redisStandaloneConfiguration.setPort(this.port);
        redisStandaloneConfiguration.setDatabase(0);

        JedisClientConfiguration.JedisClientConfigurationBuilder jedisClientConfigurationBuilder =
                JedisClientConfiguration.builder();
        /*указывается время которое отводится для подключения к базе данных, если
        * по истечении указанного времени подключение не удалось,
        * тогда выбрасывается исключение.*/
        jedisClientConfigurationBuilder.connectTimeout(Duration.ofSeconds(60));// 60s connection timeout

        JedisClientConfiguration clientConfiguration = jedisClientConfigurationBuilder.build();

        /**
         * настраивает пул подключений:
         */
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMinIdle(2); /*минимальное количество подключений*/
        poolConfig.setMaxIdle(5); /*Максимальное количество незанятых подключений,
        которое может храниться в пуле*/
        poolConfig.setMaxTotal(20);/*Максимальное количество активных соединений, которое может храниться в пуле*/

        JedisConnectionFactory connectionFactory =
                new JedisConnectionFactory(redisStandaloneConfiguration, clientConfiguration);

        return connectionFactory;
    }

    @Bean
    RedisTemplate<String, Item> redisTemplate(){
        RedisTemplate<String,Item> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory());
        return redisTemplate;
    }
 }
