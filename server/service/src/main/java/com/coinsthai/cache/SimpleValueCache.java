package com.coinsthai.cache;

import java.util.concurrent.TimeUnit;

/**
 * @author
 */
public interface SimpleValueCache<T> {

    T get(String id);

    void set(String id, T value);

    /**
     * 设置缓存
     *
     * @param id
     * @param value
     * @param expires 失效时长，单位分钟
     */
    void set(String id, T value, int expires);

    void set(String id, T value, int expires, TimeUnit unit);

    /**
     * 设置失效时间
     *
     * @param id
     * @param expires 失效时长，单位分钟
     */
    void expire(String id, int expires);

    void expire(String id, int expires, TimeUnit unit);

    void delete(String id);

}
