package com.coinsthai.module.kyc;

import com.coinsthai.module.edm.event.IdentifierEvent;

/**
 * @author
 */
public class IdCardVerifiedEvent extends IdentifierEvent {

    public IdCardVerifiedEvent() {
        super();
    }

    /**
     * @param source FaceId验证请求ID
     */
    public IdCardVerifiedEvent(String source) {
        super(source);
    }

}
