package com.coinsthai.service;

import com.coinsthai.model.Bill;
import com.coinsthai.vo.bill.BillCreateRequest;

/**
 * @author
 */
public interface TransactionService {

    Bill createBill(BillCreateRequest request);

}
