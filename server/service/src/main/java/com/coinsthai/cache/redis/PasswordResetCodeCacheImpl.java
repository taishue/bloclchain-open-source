package com.coinsthai.cache.redis;

import com.coinsthai.cache.PasswordResetCodeCache;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author
 */
@Component
public class PasswordResetCodeCacheImpl extends AbstractSimpleValueCache<String> implements PasswordResetCodeCache {

    @Override
    public void set(String id, String value) {
        super.set(id, value, 1, TimeUnit.DAYS); //默认保存1天
    }

    @Override
    protected String generateKey(String id) {
        return RedisKeys.PASSWORD_RESET_CODE + id;
    }
}
