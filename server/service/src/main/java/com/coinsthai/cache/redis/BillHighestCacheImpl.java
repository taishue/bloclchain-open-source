package com.coinsthai.cache.redis;

import com.coinsthai.cache.BillHighestCache;
import com.coinsthai.model.Bill;
import com.coinsthai.model.Deal;
import com.coinsthai.pojo.intenum.BillType;
import com.coinsthai.service.impl.bill.BillSimpleViewConverter;
import com.coinsthai.vo.bill.BillSimpleView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 价格最高的买单缓存
 * 列表的排序是根据价格由高到低排
 *
 * @author
 */
@Component
public class BillHighestCacheImpl implements BillHighestCache {

    @Autowired
    protected RedisTemplate<String, BillSimpleView> redisTemplate;

    @Autowired
    private BillSimpleViewConverter billSimpleViewConverter;

    @Override
    public List<BillSimpleView> get(String marketId) {
        String key = generateKey(marketId);
        List<BillSimpleView> list = redisTemplate.opsForList().range(key, 0, -1);
        if (list.size() >= MAX_LENGTH) {
            redisTemplate.opsForList().trim(key, 0, MAX_LENGTH - 1);
            list = redisTemplate.opsForList().range(key, 0, -1);
        }
        return list;
    }

    @Override
    public boolean cached(String marketId) {
        return redisTemplate.hasKey(generateKey(marketId));
    }

    @Override
    public void set(String marketId, List<BillSimpleView> list) {
        String key = generateKey(marketId);
        redisTemplate.delete(key);

        if (list.isEmpty()) {
            return;
        }

        List<BillSimpleView> views = list.stream().limit(MAX_LENGTH).collect(Collectors.toList());
        redisTemplate.opsForList().rightPushAll(key, views);
    }

    @Override
    public void add(Bill bill) {
        if (bill.getRemainVolume() <= 0l || bill.getType() != BillType.BUY) {
            return;
        }
        String key = generateKey(bill.getMarket().getId());
        if (!redisTemplate.hasKey(key)) {
            // 未在缓存中，不处理
            return;
        }

        BillSimpleView addingView = billSimpleViewConverter.toPojo(bill);
        List<BillSimpleView> list = redisTemplate.opsForList().range(key, 0, -1);

        BillSimpleView lastView = list.get(list.size() - 1);
        if (addingView.getPrice() < lastView.getPrice()) {
            if (list.size() < MAX_LENGTH) {
                // 放到队列最后面
                redisTemplate.opsForList().rightPush(key, addingView);
            }
            return;
        }

        boolean added = false;
        for (int i = 0; i < list.size(); i++) {
            BillSimpleView view = list.get(i);
            if (addingView.getPrice() > view.getPrice()) {
                // 添加在当前价格点之前
                redisTemplate.opsForList().leftPush(key, view, addingView);
                added = true;
                break;
            }

            if (addingView.getPrice() == view.getPrice()) {
                // 与当前价格点合并
                addingView.setVolume(addingView.getVolume() + view.getVolume());
                redisTemplate.opsForList().leftPush(key, view, addingView);
                redisTemplate.opsForList().remove(key, 1, view);
                added = true;
                break;
            }
        }

        // 比已有的价格都低，加到队尾
        if (!added) {
            // 不会执行到这里
            //redisTemplate.opsForList().rightPush(key, addingView);
        }

        // 如果长度已超过最大，则裁剪
        if (list.size() >= MAX_LENGTH) {
            redisTemplate.opsForList().trim(key, 0, MAX_LENGTH - 1);
        }
    }

    @Override
    public void remove(Bill bill) {
        if (bill.getRemainVolume() <= 0l || bill.getType() != BillType.BUY) {
            return;
        }
        String key = generateKey(bill.getMarket().getId());
        if (!redisTemplate.hasKey(key)) {
            // 未在缓存中，不处理
            return;
        }

        List<BillSimpleView> list = redisTemplate.opsForList().range(key, 0, -1);
        BillSimpleView lastView = list.get(list.size() - 1);
        if (bill.getPrice() < lastView.getPrice()) {
            // 所撤单的价格小于最后面的，则没在缓存中
            return;
        }

        for (int i = 0; i < list.size(); i++) {
            BillSimpleView view = list.get(i);
            if (bill.getPrice() == view.getPrice()) {
                if (bill.getRemainVolume() >= view.getVolume()) {
                    // 缓存数据有错，或需要重新生成
                    redisTemplate.delete(key);
                }
                else {
                    BillSimpleView updatingView = new BillSimpleView();
                    BeanUtils.copyProperties(view, updatingView);
                    updatingView.setVolume(view.getVolume() - bill.getRemainVolume());
                    redisTemplate.opsForList().leftPush(key, view, updatingView);
                    redisTemplate.opsForList().remove(key, 1, view);
                }
                return;
            }
        }

        // 未有匹配项，则表示缓存有错
        redisTemplate.delete(key);
    }

    @Override
    public void remove(Deal deal) {
        if (deal == null || deal.getVolume() <= 0l) {
            return;
        }

        String key = generateKey(deal.getMarket().getId());
        if (!redisTemplate.hasKey(key)) {
            // 未在缓存中，不处理
            return;
        }

        List<BillSimpleView> list = redisTemplate.opsForList().range(key, 0, -1);
        BillSimpleView lastView = list.get(list.size() - 1);
        if (deal.getPrice() < lastView.getPrice()) {
            // 成交的价格小于最后面的，则没在缓存中
            return;
        }

        for (int i = 0; i < list.size(); i++) {
            BillSimpleView view = list.get(i);
            if (deal.getPrice() == view.getPrice()) {
                if (deal.getVolume() >= view.getVolume()) {
                    // 缓存数据有错，或需要重新生成
                    redisTemplate.delete(key);
                }
                else {
                    BillSimpleView updatingView = new BillSimpleView();
                    BeanUtils.copyProperties(view, updatingView);
                    updatingView.setVolume(view.getVolume() - deal.getVolume());
                    redisTemplate.opsForList().leftPush(key, view, updatingView);
                    redisTemplate.opsForList().remove(key, 1, view);
                }
                return;
            }
        }

        // 未有匹配项，则表示缓存有错
        redisTemplate.delete(key);
    }

    private String generateKey(String marketId) {
        return RedisKeys.BILLS_HIGHEST + marketId;
    }

}
