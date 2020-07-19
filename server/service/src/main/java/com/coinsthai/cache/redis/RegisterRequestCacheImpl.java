package com.coinsthai.cache.redis;

import com.coinsthai.cache.RegisterRequestCache;
import com.coinsthai.vo.user.RegisterRequest;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author
 */
@Component
public class RegisterRequestCacheImpl extends AbstractSimpleValueCache<RegisterRequest> implements RegisterRequestCache {

    // 默认保存1天
    @Override
    public void set(String id, RegisterRequest value) {
        super.set(id, value, 1, TimeUnit.DAYS);
    }

    @Override
    protected String generateKey(String id) {
        return RedisKeys.REGISTER_REQUEST + id;
    }
}
