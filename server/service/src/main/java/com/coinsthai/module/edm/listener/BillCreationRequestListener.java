package com.coinsthai.module.edm.listener;

import com.coinsthai.module.edm.event.BillCreationRequestEvent;
import com.coinsthai.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author
 */
@Component
public class BillCreationRequestListener extends CompositeEventListener<BillCreationRequestEvent> {

    @Autowired
    private TransactionService transactionService;

    @Override
    public void onEvent(BillCreationRequestEvent event) {
        transactionService.createBill(event.getSource());
    }
}
