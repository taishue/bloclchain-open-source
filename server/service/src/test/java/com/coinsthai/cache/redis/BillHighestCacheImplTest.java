package com.coinsthai.cache.redis;

import com.coinsthai.cache.BillHighestCache;
import com.coinsthai.model.Bill;
import com.coinsthai.model.Deal;
import com.coinsthai.pojo.intenum.BillType;
import com.coinsthai.util.random.RandomUtils;
import com.coinsthai.vo.bill.BillSimpleView;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static com.coinsthai.test.RandomizerUtils.enhancedRandom;
import static com.coinsthai.test.RandomizerUtils.highestBuySimpleViews;
import static org.junit.Assert.*;

/**
 * @author 
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class BillHighestCacheImplTest {

    @Autowired
    private BillHighestCacheImpl billHighestCache;

    @Autowired
    private RedisTemplate redisTemplate;

    private EnhancedRandom random = enhancedRandom();

    private String marketId;

    @Before
    public void before() {
        marketId = random.nextObject(String.class);
        List<BillSimpleView> list = highestBuySimpleViews();
        billHighestCache.set(marketId, list);
    }

    @After
    public void after() {
        deleteCache(marketId);
    }

    @Test
    public void get() throws Exception {
        List<BillSimpleView> result = billHighestCache.get(marketId);
        assertNotNull(result);
        assertTrue(result.size() <= BillHighestCache.MAX_LENGTH);
    }

    @Test
    public void cached() throws Exception {
        assertTrue(billHighestCache.cached(marketId));
        assertFalse(billHighestCache.cached(random.nextObject(String.class)));
    }

    @Test
    public void set() throws Exception {
        // 正常的流程
        List<BillSimpleView> list = highestBuySimpleViews();
        billHighestCache.set(marketId, list);

        List<BillSimpleView> result = billHighestCache.get(marketId);
        assertNotNull(result);
        assertTrue(result.size() <= BillHighestCache.MAX_LENGTH);


        // 空列表
        String newMarketId = random.nextObject(String.class);
        List<BillSimpleView> emptyList = new ArrayList<>();
        billHighestCache.set(newMarketId, emptyList);

        result = billHighestCache.get(newMarketId);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void add_Illegal() throws Exception {
        // 非法的Bill
        Bill bill = random.nextObject(Bill.class);
        bill.setType(BillType.SELL);
        bill.getMarket().setId(marketId);
        billHighestCache.add(bill);

        List<BillSimpleView> result = billHighestCache.get(marketId);
        assertNotNull(result);


        // 未存在的market id
        bill = createNormalBill();
        billHighestCache.add(bill);

        result = billHighestCache.get(bill.getMarket().getId());
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void add_EmptyList() throws Exception {
        String marketId = random.nextObject(String.class);
        Bill bill = createNormalBill();
        bill.getMarket().setId(marketId);
        billHighestCache.add(bill);

        List<BillSimpleView> result = billHighestCache.get(marketId);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void add_ToLast() throws Exception {
        String marketId = random.nextObject(String.class);
        int size = BillHighestCache.MAX_LENGTH / 2;
        List<BillSimpleView> list = highestBuySimpleViews(size);
        billHighestCache.set(marketId, list);

        long minPrice = list.get(size - 1).getPrice();
        Bill bill = createNormalBill();
        bill.getMarket().setId(marketId);
        bill.setPrice(minPrice - 1);
        billHighestCache.add(bill);

        List<BillSimpleView> result = billHighestCache.get(marketId);
        assertNotNull(result);
        assertEquals(size + 1, result.size());

        deleteCache(marketId);
    }

    @Test
    public void add_TooSmall() throws Exception {
        String marketId = random.nextObject(String.class);
        int size = BillHighestCache.MAX_LENGTH;
        List<BillSimpleView> list = highestBuySimpleViews(size);
        billHighestCache.set(marketId, list);

        long minPrice = list.get(size - 1).getPrice();
        Bill bill = createNormalBill();
        bill.getMarket().setId(marketId);
        bill.setPrice(minPrice - 1);
        billHighestCache.add(bill);

        List<BillSimpleView> result = billHighestCache.get(marketId);
        assertNotNull(result);
        assertEquals(BillHighestCache.MAX_LENGTH, result.size());

        deleteCache(marketId);
    }

    @Test
    public void add_ToBefore() throws Exception {
        List<BillSimpleView> beforeList = billHighestCache.get(marketId);
        int index = RandomUtils.nextInt(0, beforeList.size() - 1);
        long indexPrice = beforeList.get(index).getPrice();

        Bill bill = createNormalBill();
        bill.getMarket().setId(marketId);
        bill.setPrice(indexPrice + 1);
        billHighestCache.add(bill);

        List<BillSimpleView> result = billHighestCache.get(marketId);
        assertNotNull(result);
        if (beforeList.size() < BillHighestCache.MAX_LENGTH) {
            assertEquals(beforeList.size() + 1, result.size());
        }
        else {
            assertEquals(BillHighestCache.MAX_LENGTH, result.size());
        }
    }

    @Test
    public void add_Merge() throws Exception {
        List<BillSimpleView> beforeList = billHighestCache.get(marketId);
        int index = RandomUtils.nextInt(0, beforeList.size() - 1);
        long indexPrice = beforeList.get(index).getPrice();

        Bill bill = createNormalBill();
        bill.getMarket().setId(marketId);
        bill.setPrice(indexPrice);
        billHighestCache.add(bill);

        List<BillSimpleView> result = billHighestCache.get(marketId);
        assertNotNull(result);
        assertEquals(beforeList.size(), result.size());
    }

    @Test
    public void add_Cut() throws Exception {
        String marketId = random.nextObject(String.class);
        int size = BillHighestCache.MAX_LENGTH;
        List<BillSimpleView> list = highestBuySimpleViews(size);
        billHighestCache.set(marketId, list);

        int index = RandomUtils.nextInt(0, BillHighestCache.MAX_LENGTH - 1);
        long indexPrice = list.get(index).getPrice();
        Bill bill = createNormalBill();
        bill.getMarket().setId(marketId);
        bill.setPrice(indexPrice + 1);
        billHighestCache.add(bill);

        List<BillSimpleView> result = billHighestCache.get(marketId);
        assertNotNull(result);
        assertEquals(BillHighestCache.MAX_LENGTH, result.size());

        deleteCache(marketId);
    }

    @Test
    public void removeByBill_Illegal() throws Exception {
        List<BillSimpleView> beforeList = billHighestCache.get(marketId);
        Bill bill = createNormalBill();
        bill.getMarket().setId(marketId);
        bill.setRemainVolume(-1l);
        billHighestCache.remove(bill);

        List<BillSimpleView> result = billHighestCache.get(marketId);
        assertNotNull(result);
        assertEquals(beforeList.size(), result.size());


        bill = createNormalBill();
        bill.getMarket().setId(marketId);
        bill.setType(BillType.SELL);
        billHighestCache.remove(bill);

        result = billHighestCache.get(marketId);
        assertNotNull(result);
        assertEquals(beforeList.size(), result.size());
    }

    @Test
    public void removeByBill_NotExists() throws Exception {
        List<BillSimpleView> beforeList = billHighestCache.get(marketId);
        long minPrice = beforeList.get(beforeList.size() - 1).getPrice();

        Bill bill = createNormalBill();
        bill.setPrice(minPrice - 1);
        bill.getMarket().setId(marketId);
        billHighestCache.remove(bill);

        List<BillSimpleView> result = billHighestCache.get(marketId);
        assertNotNull(result);
        assertTrue(result.size() == beforeList.size());
    }

    @Test
    public void removeByBill_WrongVolume() throws Exception {
        List<BillSimpleView> beforeList = billHighestCache.get(marketId);
        int index = RandomUtils.nextInt(0, beforeList.size() - 1);
        BillSimpleView indexView = beforeList.get(index);

        Bill bill = createNormalBill();
        bill.getMarket().setId(marketId);
        bill.setPrice(indexView.getPrice());
        bill.setRemainVolume(indexView.getVolume() + 1);
        billHighestCache.remove(bill);

        List<BillSimpleView> result = billHighestCache.get(marketId);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void removeByBill_NotMatch() throws Exception {
        List<BillSimpleView> beforeList = billHighestCache.get(marketId);
        int index = RandomUtils.nextInt(0, beforeList.size() - 1);
        long indexPrice = beforeList.get(index).getPrice();

        Bill bill = createNormalBill();
        bill.getMarket().setId(marketId);
        bill.setPrice(indexPrice + 1);
        billHighestCache.remove(bill);

        List<BillSimpleView> result = billHighestCache.get(marketId);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void removeByBill_NoCache() throws Exception {
        Bill bill = createNormalBill();
        billHighestCache.remove(bill);

        List<BillSimpleView> result = billHighestCache.get(bill.getMarket().getId());
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }


    @Test
    public void removeByBill() throws Exception {
        List<BillSimpleView> beforeList = billHighestCache.get(marketId);
        int index = RandomUtils.nextInt(0, beforeList.size() - 1);
        BillSimpleView indexView = beforeList.get(index);
        long remainVolume = RandomUtils.nextLong(1, indexView.getVolume() - 1);

        Bill bill = createNormalBill();
        bill.getMarket().setId(marketId);
        bill.setPrice(indexView.getPrice());
        bill.setRemainVolume(remainVolume);
        billHighestCache.remove(bill);

        List<BillSimpleView> result = billHighestCache.get(marketId);
        assertNotNull(result);
        assertTrue(result.size() == beforeList.size());
        assertEquals(indexView.getVolume() - remainVolume, result.get(index).getVolume());
    }

    @Test
    public void removeByDeal_Illegal() throws Exception {
        List<BillSimpleView> beforeList = billHighestCache.get(marketId);
        billHighestCache.remove((Deal) null);

        List<BillSimpleView> result = billHighestCache.get(marketId);
        assertNotNull(result);
        assertTrue(result.size() == beforeList.size());


        Deal deal = random.nextObject(Deal.class);
        deal.getMarket().setId(marketId);
        deal.setVolume(0l);
        billHighestCache.remove(deal);

        result = billHighestCache.get(marketId);
        assertNotNull(result);
        assertTrue(result.size() <= beforeList.size());
    }


    @Test
    public void removeByDeal_WrongVolume() throws Exception {
        List<BillSimpleView> beforeList = billHighestCache.get(marketId);
        int index = RandomUtils.nextInt(0, beforeList.size() - 1);
        BillSimpleView indexView = beforeList.get(index);

        Deal deal = random.nextObject(Deal.class);
        deal.getMarket().setId(marketId);
        deal.setPrice(indexView.getPrice());
        deal.setVolume(indexView.getVolume());
        billHighestCache.remove(deal);

        List<BillSimpleView> result = billHighestCache.get(marketId);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void removeByDeal_NotMatch() throws Exception {
        List<BillSimpleView> beforeList = billHighestCache.get(marketId);
        int index = RandomUtils.nextInt(0, beforeList.size() - 1);
        BillSimpleView indexView = beforeList.get(index);

        Deal deal = random.nextObject(Deal.class);
        deal.getMarket().setId(marketId);
        deal.setPrice(indexView.getPrice() - 1);
        billHighestCache.remove(deal);

        List<BillSimpleView> result = billHighestCache.get(marketId);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void removeByDeal_NotExists() throws Exception {
        List<BillSimpleView> beforeList = billHighestCache.get(marketId);
        long minPrice = beforeList.get(beforeList.size() - 1).getPrice();

        Deal deal = random.nextObject(Deal.class);
        deal.getMarket().setId(marketId);
        deal.setPrice(minPrice - 1);
        billHighestCache.remove(deal);

        List<BillSimpleView> result = billHighestCache.get(marketId);
        assertNotNull(result);
        assertTrue(result.size() == beforeList.size());
    }

    @Test
    public void removeByDeal_NoCache() throws Exception {
        Deal deal = random.nextObject(Deal.class);
        billHighestCache.remove(deal);

        List<BillSimpleView> result = billHighestCache.get(deal.getMarket().getId());
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void remove_ByDeal() throws Exception {
        List<BillSimpleView> beforeList = billHighestCache.get(marketId);
        int index = RandomUtils.nextInt(0, beforeList.size() - 1);
        BillSimpleView indexView = beforeList.get(index);
        long volume = RandomUtils.nextLong(1, indexView.getVolume() - 1);

        Deal deal = random.nextObject(Deal.class);
        deal.getMarket().setId(marketId);
        deal.setPrice(indexView.getPrice());
        deal.setVolume(volume);
        billHighestCache.remove(deal);

        List<BillSimpleView> result = billHighestCache.get(marketId);
        assertNotNull(result);
        assertTrue(result.size() == beforeList.size());
        assertEquals(indexView.getVolume() - volume, result.get(index).getVolume());
    }

    private void deleteCache(String marketId) {
        String key = generateKey(marketId);
        redisTemplate.delete(key);
    }

    private String generateKey(String marketId) {
        return ReflectionTestUtils.invokeMethod(billHighestCache, "generateKey", marketId);
    }

    private Bill createNormalBill() {
        Bill bill = random.nextObject(Bill.class);
        if (bill.getVolume() <= 0) {
            bill.setVolume(1l);
        }
        bill.setType(BillType.BUY);
        return bill;
    }


}