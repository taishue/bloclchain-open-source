package com.coinsthai.service.impl.bill;

import com.coinsthai.model.Bill;
import com.coinsthai.model.Deal;
import com.coinsthai.pojo.intenum.BillType;
import com.coinsthai.repository.DealRepository;

import java.util.List;

/**
 * @author
 */
public class BillUtils {

    public static long computeAveragePrice(Bill bill, DealRepository dealRepository) {
        List<Deal> deals;
        if (bill.getType() == BillType.SELL) {
            deals = dealRepository.findBySellId(bill.getId());
        }
        else {
            deals = dealRepository.findByBuyId(bill.getId());
        }

        double total = 0d;
        for (Deal deal : deals) {
            total += deal.getVolume() * deal.getPrice();
        }
        return Math.round(total / (bill.getVolume() - bill.getRemainVolume()));
    }
}
