package com.coinsthai.module.edm.event;

import com.coinsthai.module.edm.EventObject;
import com.coinsthai.vo.bill.BillCreateRequest;

/**
 * @author
 */
public class BillCreationRequestEvent extends EventObject<BillCreateRequest> {

    public BillCreationRequestEvent() {
    }

    public BillCreationRequestEvent(BillCreateRequest source) {
        super(source);
    }

    @Override
    public String getKey() {
        return getSource().toString();
    }

}
