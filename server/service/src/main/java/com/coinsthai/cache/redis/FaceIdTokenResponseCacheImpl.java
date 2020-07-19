package com.coinsthai.cache.redis;

import com.coinsthai.cache.FaceIdTokenResponseCache;
import com.coinsthai.module.kyc.FaceIdTokenResponse;
import org.springframework.stereotype.Component;

/**
 * @author
 */
@Component
public class FaceIdTokenResponseCacheImpl extends AbstractSimpleValueCache<FaceIdTokenResponse>
        implements FaceIdTokenResponseCache {

    @Override
    protected String generateKey(String id) {
        return RedisKeys.FACEID_TOKEN + id;
    }
}
