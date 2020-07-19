package com.coinsthai.cache.redis;

/**
 * @author 
 */
public interface RedisKeys {

    String NAME = "name:";

    String COINS_ALL = "coin:all";

    String COIN = "coin:";

    String MARKETS_ALL = "markets:all";

    String MARKETS_HOTS = "markets:hots";

    String MARKETS_BASE_GROUPED = "markets:";

    String MARKET = "market:";

    String MARKET_TREND = "mktr:";

    String MARKET_TREND_EXTERNAL = "mktrex:";

    String KLINE = "klines:";

    String REGISTER_REQUEST = "register:";

    String PASSWORD_RESET_CODE = "pwdrc:";

    String BILLS = "bills:";

    String BILLS_LOWEST = "bills:low:";

    String BILLS_HIGHEST = "bills:high:";

    String LATEST_BLOCK = "latestBlock:";

    String PASSCODE = "passcode:";

    String FACEID_TOKEN = "facetoken:";

}
