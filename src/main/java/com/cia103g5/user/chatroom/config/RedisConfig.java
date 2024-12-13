//package com.cia103g5.user.chatroom.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//
//import com.cia103g5.user.chatroom.model.RedisService;
//
//@Configuration
//public class RedisConfig {
//
//    @Bean
//    public RedisService redisService(RedisTemplate<String, Object> redisTemplate) {
//        return new RedisService(redisTemplate);
//    }
//    
//    @Bean
//    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
//        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
//        redisTemplate.setConnectionFactory(redisConnectionFactory);
//
//        // 設置 Key 的序列化方式為 StringRedisSerializer
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
//
//        // 設置 Hash Key 的序列化方式為 StringRedisSerializer
//        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
//
//        // 設置 Value 和 Hash Value 的序列化方式為 StringRedisSerializer
//        redisTemplate.setValueSerializer(new StringRedisSerializer());
//        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
//
//        return redisTemplate;
//    }
//    
//    @Bean
//    public RedisConnectionFactory redisConnectionFactory() {
//        // 手動定義連接工廠，使用 application.yml 的配置
//        return new LettuceConnectionFactory("localhost", 6379);
//    }
//    
//}
