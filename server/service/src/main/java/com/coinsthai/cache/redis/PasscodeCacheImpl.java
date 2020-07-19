package com.coinsthai.cache.redis;

import com.coinsthai.cache.PasscodeCache;
import com.coinsthai.module.passcode.PasscodeContent;
import org.springframework.stereotype.Component;

/**
 * @author 
 */
@Component
public class PasscodeCacheImpl extends AbstractSimpleValueCache<PasscodeContent> implements PasscodeCache {

    @Override
    protected String generateKey(String id) {
        return RedisKeys.PASSCODE + id;
    }
}
