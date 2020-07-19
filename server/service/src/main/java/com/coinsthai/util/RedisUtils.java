package com.coinsthai.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.nio.charset.Charset;

/**
 * @author
 */
public class RedisUtils {

    /**
     * 创建Redis序列化对象
     *
     * @return
     */
    private static Jackson2JsonRedisSerializer createJackson2JsonRedisSerializer() {
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer =
                new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        return jackson2JsonRedisSerializer;
    }

    /**
     * 创建RedisTemplate
     *
     * @return
     */
    public static RedisTemplate createRedisTemplate(RedisConnectionFactory factory) {
        RedisTemplate result = new RedisTemplate();
        result.setConnectionFactory(factory);
        StringRedisSerializer keySerializer =
                new StringRedisSerializer(Charset.forName("UTF-8"));
        result.setKeySerializer(keySerializer);
        //result.setHashKeySerializer(keySerializer);
        Jackson2JsonRedisSerializer jsonRedisSerializer =
                createJackson2JsonRedisSerializer();
        result.setValueSerializer(jsonRedisSerializer);
        //result.setHashValueSerializer(jsonRedisSerializer);
        return result;
    }

}
