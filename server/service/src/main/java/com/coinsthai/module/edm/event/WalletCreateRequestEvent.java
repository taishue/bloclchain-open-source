package com.coinsthai.module.edm.event;

import com.coinsthai.module.edm.EventObject;
import com.coinsthai.vo.wallet.WalletCreateRequest;

/**
 * @author
 */
public class WalletCreateRequestEvent extends EventObject<WalletCreateRequest> {

    public WalletCreateRequestEvent() {
        super();
    }

    public WalletCreateRequestEvent(WalletCreateRequest source) {
        super(source);
    }

    @Override
    public String getKey() {
        return getSource().toString();
    }
}
