package com.coinsthai.cache.redis;

import com.coinsthai.cache.LatestBlockCache;
import com.coinsthai.btc.LatestBlock;
import org.springframework.stereotype.Component;

/**
 * @author YeYifeng
 */
@Component
public class LatestBlockCacheImpl extends AbstractSimpleValueCache<LatestBlock> implements LatestBlockCache {

	@Override
	protected String generateKey(String id) {
		return RedisKeys.LATEST_BLOCK + id;
	}
}
