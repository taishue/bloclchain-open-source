package com.coinsthai.cache.redis;

import com.coinsthai.cache.BillLowestCache;
import com.coinsthai.model.Bill;
import com.coinsthai.model.Deal;
import com.coinsthai.pojo.intenum.BillType;
import com.coinsthai.test.RandomizerUtils;
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

import static org.junit.Assert.*;

/**
 * @author
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class BillLowestCacheImplTest {

    @Autowired
    private BillLowestCacheImpl billLowestCache;

    @Autowired
    private RedisTemplate redisTemplate;

    private EnhancedRandom random = RandomizerUtils.enhancedRandom();

    private String marketId;

    @Before
    public void before() {
        marketId = random.nextObject(String.class);
        List<BillSimpleView> list = RandomizerUtils.lowestSellSimpleViews();
        billLowestCache.set(marketId, list);
    }

    @After
    public void after() {
        deleteCache(marketId);
    }

    @Test
    public void get() throws Exception {
        List<BillSimpleView> result = billLowestCache.get(marketId);
        assertNotNull(result);
        assertTrue(result.size() <= BillLowestCache.MAX_LENGTH);
    }

    @Test
    public void cached() throws Exception {
        assertTrue(billLowestCache.cached(marketId));
        assertFalse(billLowestCache.cached(random.nextObject(String.class)));
    }

    @Test
    public void set_EmptyList() {
        String newMarketId = random.nextObject(String.class);
        List<BillSimpleView> emptyList = new ArrayList<>();
        billLowestCache.set(newMarketId, emptyList);

        List<BillSimpleView> result = billLowestCache.get(newMarketId);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void set_Cut() {
        int size = RandomUtils.nextInt(BillLowestCache.MAX_LENGTH + 1, BillLowestCache.MAX_LENGTH * 2);
        List<BillSimpleView> list = RandomizerUtils.lowestSellSimpleViews(size);
        billLowestCache.set(marketId, list);

        List<BillSimpleView> result = billLowestCache.get(marketId);
        assertNotNull(result);
        assertTrue(result.size() == BillLowestCache.MAX_LENGTH);
        assertEquals(list.get(list.size() - 1).getPrice(), result.get(result.size() - 1).getPrice());
    }

    @Test
    public void set() throws Exception {
        // 正常的流程
        List<BillSimpleView> list = RandomizerUtils.lowestSellSimpleViews();
        billLowestCache.set(marketId, list);

        List<BillSimpleView> result = billLowestCache.get(marketId);
        assertNotNull(result);
        assertTrue(result.size() <= BillLowestCache.MAX_LENGTH);
        assertEquals(list.get(list.size() - 1).getPrice(), result.get(result.size() - 1).getPrice());
    }

    @Test
    public void add_Illegal() throws Exception {
        // 非法的Bill
        Bill bill = random.nextObject(Bill.class);
        bill.setType(BillType.BUY);
        bill.getMarket().setId(marketId);
        billLowestCache.add(bill);

        List<BillSimpleView> result = billLowestCache.get(marketId);
        assertNotNull(result);


        // 未存在的market id
        bill = createNormalBill();
        billLowestCache.add(bill);

        result = billLowestCache.get(bill.getMarket().getId());
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void add_EmptyList() throws Exception {
        String marketId = random.nextObject(String.class);
        Bill bill = createNormalBill();
        bill.getMarket().setId(marketId);
        billLowestCache.add(bill);

        List<BillSimpleView> result = billLowestCache.get(marketId);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void add_ToFirst() throws Exception {
        String marketId = random.nextObject(String.class);
        int size = BillLowestCache.MAX_LENGTH / 2;
        List<BillSimpleView> list = RandomizerUtils.lowestSellSimpleViews(size);
        billLowestCache.set(marketId, list);

        long maxPrice = list.get(0).getPrice();
        Bill bill = createNormalBill();
        bill.getMarket().setId(marketId);
        bill.setPrice(maxPrice + 1);
        billLowestCache.add(bill);

        List<BillSimpleView> result = billLowestCache.get(marketId);
        assertNotNull(result);
        assertEquals(size + 1, result.size());
        assertEquals(bill.getPrice(), result.get(0).getPrice());

        deleteCache(marketId);
    }

    @Test
    public void add_TooHigh() throws Exception {
        String marketId = random.nextObject(String.class);
        int size = BillLowestCache.MAX_LENGTH;
        List<BillSimpleView> list = RandomizerUtils.lowestSellSimpleViews(size);
        billLowestCache.set(marketId, list);

        long maxPrice = list.get(0).getPrice();
        Bill bill = createNormalBill();
        bill.getMarket().setId(marketId);
        bill.setPrice(maxPrice + 1);
        billLowestCache.add(bill);

        List<BillSimpleView> result = billLowestCache.get(marketId);
        assertNotNull(result);
        assertEquals(BillLowestCache.MAX_LENGTH, result.size());

        deleteCache(marketId);
    }

    @Test
    public void add_ToBefore() throws Exception {
        int size = RandomUtils.nextInt(BillLowestCache.MAX_LENGTH / 2, BillLowestCache.MAX_LENGTH);
        List<BillSimpleView> beforeList = RandomizerUtils.lowestSellSimpleViews(size);
        billLowestCache.set(marketId, beforeList);

        int index = RandomUtils.nextInt(1, beforeList.size() - 1);
        long indexPrice = beforeList.get(index).getPrice();

        Bill bill = createNormalBill();
        bill.getMarket().setId(marketId);
        bill.setPrice(indexPrice + 1);
        billLowestCache.add(bill);

        List<BillSimpleView> result = billLowestCache.get(marketId);
        assertNotNull(result);
        if (beforeList.size() < BillLowestCache.MAX_LENGTH) {
            assertEquals(beforeList.size() + 1, result.size());
        }
        else {
            assertEquals(BillLowestCache.MAX_LENGTH, result.size());
        }
    }

    @Test
    public void add_Merge() throws Exception {
        List<BillSimpleView> beforeList = billLowestCache.get(marketId);
        int index = RandomUtils.nextInt(0, beforeList.size() - 1);
        long indexPrice = beforeList.get(index).getPrice();

        Bill bill = createNormalBill();
        bill.getMarket().setId(marketId);
        bill.setPrice(indexPrice);
        billLowestCache.add(bill);

        List<BillSimpleView> result = billLowestCache.get(marketId);
        assertNotNull(result);
        assertEquals(beforeList.size(), result.size());
    }

    @Test
    public void add_ToLast() throws Exception {
        List<BillSimpleView> beforeList = billLowestCache.get(marketId);
        long minPrice = beforeList.get(beforeList.size() - 1).getPrice();

        Bill bill = createNormalBill();
        bill.getMarket().setId(marketId);
        bill.setPrice(minPrice - 1);
        billLowestCache.add(bill);

        List<BillSimpleView> result = billLowestCache.get(marketId);
        assertNotNull(result);
        if (beforeList.size() < BillLowestCache.MAX_LENGTH) {
            assertEquals(beforeList.size() + 1, result.size());
        }
        else {
            assertEquals(BillLowestCache.MAX_LENGTH, result.size());
        }
    }

    @Test
    public void add_Cut() throws Exception {
        int size = BillLowestCache.MAX_LENGTH;
        List<BillSimpleView> list = RandomizerUtils.lowestSellSimpleViews(size);
        billLowestCache.set(marketId, list);

        int index = RandomUtils.nextInt(0, BillLowestCache.MAX_LENGTH - 1);
        long indexPrice = list.get(index).getPrice();
        Bill bill = createNormalBill();
        bill.getMarket().setId(marketId);
        bill.setPrice(indexPrice - 1);
        billLowestCache.add(bill);

        List<BillSimpleView> result = billLowestCache.get(marketId);
        assertNotNull(result);
        assertEquals(BillLowestCache.MAX_LENGTH, result.size());
    }

    @Test
    public void removeByBill_Illegal() throws Exception {
        List<BillSimpleView> beforeList = billLowestCache.get(marketId);
        Bill bill = createNormalBill();
        bill.getMarket().setId(marketId);
        bill.setRemainVolume(-1l);
        billLowestCache.remove(bill);

        List<BillSimpleView> result = billLowestCache.get(marketId);
        assertNotNull(result);
        assertEquals(beforeList.size(), result.size());


        bill = createNormalBill();
        bill.getMarket().setId(marketId);
        bill.setType(BillType.BUY);
        billLowestCache.remove(bill);

        result = billLowestCache.get(marketId);
        assertNotNull(result);
        assertEquals(beforeList.size(), result.size());
    }

    @Test
    public void removeByBill_NotExists() throws Exception {
        List<BillSimpleView> beforeList = billLowestCache.get(marketId);
        billLowestCache.set(marketId, beforeList);
        long maxPrice = beforeList.get(0).getPrice();

        Bill bill = createNormalBill();
        bill.setPrice(maxPrice + 1);
        bill.getMarket().setId(marketId);
        billLowestCache.remove(bill);

        List<BillSimpleView> result = billLowestCache.get(marketId);
        assertNotNull(result);
        assertTrue(result.size() == beforeList.size());
    }

    @Test
    public void removeByBill_WrongVolume() throws Exception {
        int size = RandomUtils.nextInt(1, BillLowestCache.MAX_LENGTH);
        List<BillSimpleView> beforeList = RandomizerUtils.lowestSellSimpleViews(size);
        billLowestCache.set(marketId, beforeList);
        int index = RandomUtils.nextInt(0, beforeList.size() - 1);
        BillSimpleView indexView = beforeList.get(index);

        Bill bill = createNormalBill();
        bill.getMarket().setId(marketId);
        bill.setPrice(indexView.getPrice());
        bill.setRemainVolume(indexView.getVolume() + 1);
        billLowestCache.remove(bill);

        List<BillSimpleView> result = billLowestCache.get(marketId);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void removeByBill_NotMatch() throws Exception {
        List<BillSimpleView> beforeList = billLowestCache.get(marketId);
        int index = RandomUtils.nextInt(0, beforeList.size() - 1);
        long indexPrice = beforeList.get(index).getPrice();

        Bill bill = createNormalBill();
        bill.getMarket().setId(marketId);
        bill.setPrice(indexPrice - 1);
        billLowestCache.remove(bill);

        List<BillSimpleView> result = billLowestCache.get(marketId);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void removeByBill_NoCache() throws Exception {
        Bill bill = createNormalBill();
        billLowestCache.remove(bill);

        List<BillSimpleView> result = billLowestCache.get(bill.getMarket().getId());
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }


    @Test
    public void removeByBill() throws Exception {
        List<BillSimpleView> beforeList = billLowestCache.get(marketId);
        int index = RandomUtils.nextInt(0, beforeList.size() - 1);
        BillSimpleView indexView = beforeList.get(index);
        long remainVolume = RandomUtils.nextLong(1, indexView.getVolume() - 1);

        Bill bill = createNormalBill();
        bill.getMarket().setId(marketId);
        bill.setPrice(indexView.getPrice());
        bill.setRemainVolume(remainVolume);
        billLowestCache.remove(bill);

        List<BillSimpleView> result = billLowestCache.get(marketId);
        assertNotNull(result);
        assertTrue(result.size() == beforeList.size());
        assertEquals(indexView.getVolume() - remainVolume, result.get(index).getVolume());
    }

    @Test
    public void removeByDeal_Illegal() throws Exception {
        List<BillSimpleView> beforeList = billLowestCache.get(marketId);
        billLowestCache.remove((Deal) null);

        List<BillSimpleView> result = billLowestCache.get(marketId);
        assertNotNull(result);
        assertTrue(result.size() == beforeList.size());


        Deal deal = random.nextObject(Deal.class);
        deal.getMarket().setId(marketId);
        deal.setVolume(0l);
        billLowestCache.remove(deal);

        result = billLowestCache.get(marketId);
        assertNotNull(result);
        assertTrue(result.size() <= beforeList.size());
    }


    @Test
    public void removeByDeal_WrongVolume() throws Exception {
        List<BillSimpleView> beforeList = billLowestCache.get(marketId);
        int index = RandomUtils.nextInt(0, beforeList.size() - 1);
        BillSimpleView indexView = beforeList.get(index);

        Deal deal = random.nextObject(Deal.class);
        deal.getMarket().setId(marketId);
        deal.setPrice(indexView.getPrice());
        deal.setVolume(indexView.getVolume());
        billLowestCache.remove(deal);

        List<BillSimpleView> result = billLowestCache.get(marketId);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void removeByDeal_NotMatch() throws Exception {
        List<BillSimpleView> beforeList = billLowestCache.get(marketId);
        int index = RandomUtils.nextInt(0, beforeList.size() - 1);
        BillSimpleView indexView = beforeList.get(index);

        Deal deal = random.nextObject(Deal.class);
        deal.getMarket().setId(marketId);
        deal.setPrice(indexView.getPrice() - 1);
        billLowestCache.remove(deal);

        List<BillSimpleView> result = billLowestCache.get(marketId);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void removeByDeal_TooHigh() throws Exception {
        List<BillSimpleView> beforeList = billLowestCache.get(marketId);
        long minPrice = beforeList.get(0).getPrice();

        Deal deal = random.nextObject(Deal.class);
        deal.getMarket().setId(marketId);
        deal.setPrice(minPrice + 1);
        billLowestCache.remove(deal);

        List<BillSimpleView> result = billLowestCache.get(marketId);
        assertNotNull(result);
        assertTrue(result.size() == beforeList.size());
    }

    @Test
    public void removeByDeal_NoCache() throws Exception {
        Deal deal = random.nextObject(Deal.class);
        billLowestCache.remove(deal);

        List<BillSimpleView> result = billLowestCache.get(deal.getMarket().getId());
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void remove_ByDeal() throws Exception {
        List<BillSimpleView> beforeList = billLowestCache.get(marketId);
        int index = RandomUtils.nextInt(0, beforeList.size() - 1);
        BillSimpleView indexView = beforeList.get(index);
        long volume = RandomUtils.nextLong(1, indexView.getVolume() - 1);

        Deal deal = random.nextObject(Deal.class);
        deal.getMarket().setId(marketId);
        deal.setPrice(indexView.getPrice());
        deal.setVolume(volume);
        billLowestCache.remove(deal);

        List<BillSimpleView> result = billLowestCache.get(marketId);
        assertNotNull(result);
        assertTrue(result.size() == beforeList.size());
        assertEquals(indexView.getVolume() - volume, result.get(index).getVolume());
    }

    private void deleteCache(String marketId) {
        String key = generateKey(marketId);
        redisTemplate.delete(key);
    }

    private String generateKey(String marketId) {
        return ReflectionTestUtils.invokeMethod(billLowestCache, "generateKey", marketId);
    }

    private Bill createNormalBill() {
        Bill bill = random.nextObject(Bill.class);
        if (bill.getVolume() <= 0) {
            bill.setVolume(1l);
        }
        bill.setType(BillType.SELL);
        return bill;
    }

}