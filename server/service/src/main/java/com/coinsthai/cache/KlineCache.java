package com.coinsthai.cache;

import com.coinsthai.pojo.intenum.KlineType;
import com.coinsthai.vo.KlineView;

import java.util.List;

/**
 * @author
 */
public interface KlineCache {

    int MAX_LENGTH = 160;

    void add(String marketId, KlineType type, KlineView view);

    void set(String marketId, KlineType type, List<KlineView> views);

    List<KlineView> getAll(String marketId, KlineType type);

    KlineView getLatest(String marketId, KlineType type);
}
