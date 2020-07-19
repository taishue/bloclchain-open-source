package com.coinsthai.test;

import com.coinsthai.pojo.common.Constant;
import com.coinsthai.pojo.intenum.BillType;
import com.coinsthai.util.random.RandomUtils;
import com.coinsthai.vo.bill.BillSimpleView;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.FieldDefinitionBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import io.github.benas.randombeans.randomizers.range.IntegerRangeRandomizer;
import io.github.benas.randombeans.randomizers.range.LongRangeRandomizer;

import java.nio.charset.Charset;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author
 */
public class RandomizerUtils {

    public static EnhancedRandom enhancedRandom() {

        EnhancedRandomBuilder builder = EnhancedRandomBuilder.aNewEnhancedRandomBuilder()
                                                             .charset(Charset.forName("UTF-8"))
                                                             .overrideDefaultInitialization(true);

        LongRangeRandomizer volumePrice = new LongRangeRandomizer(0l, Long.MAX_VALUE - 99999);
        builder.randomize(FieldDefinitionBuilder.field().named("volume").ofType(Long.TYPE).get(), volumePrice);
        builder.randomize(FieldDefinitionBuilder.field().named("remainVolume").ofType(Long.TYPE).get(), volumePrice);
        builder.randomize(FieldDefinitionBuilder.field().named("price").ofType(Long.TYPE).get(), volumePrice);
        builder.randomize(FieldDefinitionBuilder.field().named("averagePrice").ofType(Long.TYPE).get(), volumePrice);

        builder.randomize(FieldDefinitionBuilder.field().named("brokerageRate").ofType(Integer.TYPE).get(),
                          new IntegerRangeRandomizer(0, Constant.BROKERAGE_RATE_UNIT));

        return builder.build();
    }

    public static List<BillSimpleView> billSimpleViews(int size, BillType type) {
        EnhancedRandom random = enhancedRandom();
        List<BillSimpleView> list = random.objects(BillSimpleView.class, size).collect(Collectors.toList());
        if (type != null) {
            list.forEach(view -> view.setType(BillType.SELL));
        }
        return list;
    }

    public static List<BillSimpleView> highestBuySimpleViews() {
        int size = RandomUtils.nextInt(1, 20);
        return highestBuySimpleViews(size);
    }

    public static List<BillSimpleView> highestBuySimpleViews(int size) {
        List<BillSimpleView> list = billSimpleViews(size, BillType.BUY);
        list.sort(new PriceDescComparator());
        return list;
    }

    public static List<BillSimpleView> lowestSellSimpleViews() {
        int size = RandomUtils.nextInt(1, 20);
        return lowestSellSimpleViews(size);
    }

    public static List<BillSimpleView> lowestSellSimpleViews(int size) {
        List<BillSimpleView> list = billSimpleViews(size, BillType.SELL);
        list.sort(new PriceDescComparator());
        return list;
    }

    static class PriceDescComparator implements Comparator<BillSimpleView> {
        @Override
        public int compare(BillSimpleView o1, BillSimpleView o2) {
            long diff = o2.getPrice() - o1.getPrice();
            if (diff > 0) {
                return 1;
            }
            else if (diff < 0) {
                return -1;
            }
            else {
                return 0;
            }
        }
    }
}
