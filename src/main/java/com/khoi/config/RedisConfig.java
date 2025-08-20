package com.khoi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        /*
            Cấu hình serializer để tránh lỗi kiểu dữ liệu, Nếu không cấu hình
            serializer, khi lưu object sẽ dễ gặp lỗi kiểu dữ liệu hoặc không đọc được.

           Tóm lại dùng RedisTemplate khi :
                Cần thao tác trực tiếp với Redis
                Cần lưu danh sách, set, hash,...
                Cần xử lý TTL, queue, pub/sub,...
         */
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        return template;
    }
}
