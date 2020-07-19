package com.coinsthai.model.converter;

import com.coinsthai.pojo.intenum.WithdrawStatus;

/**
 * @author
 */
public class WithdrawStatusConverter extends IntEnumConverter<WithdrawStatus> {

    @Override
    public WithdrawStatus convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }

        for (WithdrawStatus intenum : WithdrawStatus.values()) {
            if (intenum.getNumber() == dbData) {
                return intenum;
            }
        }
        return null;
    }
}
