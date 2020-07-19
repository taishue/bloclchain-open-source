package com.coinsthai.security;

import in.clouthink.daas.security.token.core.Token;
import in.clouthink.daas.security.token.core.User;
import in.clouthink.daas.security.token.spi.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

/**
 * @author 
 */
public class RedisTokenProvider implements TokenProvider<Token> {

    private static final String KEY_PREFIX = "token:";

    @Autowired
    private RedisTemplate<String, Token> redisTemplateToken;

    @Override
    public void saveToken(Token token) {
        redisTemplateToken.opsForValue().set(KEY_PREFIX + token.getToken(), token);
        redisTemplateToken.expireAt(KEY_PREFIX + token.getToken(), token.getExpiredDate());
    }

    @Override
    public Token findByToken(String token) {
        return redisTemplateToken.opsForValue().get(KEY_PREFIX + token);
    }

    @Override
    public void revokeToken(Token token) {
        redisTemplateToken.delete(KEY_PREFIX + token.getToken());
    }

    @Override
    public List<Token> findByUser(User user) {
        return null;
    }
}
