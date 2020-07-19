package com.coinsthai.converter;

import com.coinsthai.model.IdCard;
import com.coinsthai.vo.user.IdCardView;
import org.springframework.stereotype.Component;

/**
 * @author
 */
@Component
public class IdCardViewConverter extends BeanCopyPojoConverter<IdCardView, IdCard> {

    @Override
    protected void afterBeanCopy(IdCard source, IdCardView target) {
        super.afterBeanCopy(source, target);
        target.setUserId(source.getUser().getId());
    }
}
