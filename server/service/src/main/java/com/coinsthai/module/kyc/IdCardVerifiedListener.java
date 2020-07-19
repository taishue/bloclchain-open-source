package com.coinsthai.module.kyc;

import com.coinsthai.module.edm.listener.CompositeEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author
 */
@Component
public class IdCardVerifiedListener extends CompositeEventListener<IdCardVerifiedEvent> {

    @Autowired
    private FaceIdService faceIdService;

    @Override
    public void onEvent(IdCardVerifiedEvent event) {
        faceIdService.requestResult(event.getSource());
    }
}
