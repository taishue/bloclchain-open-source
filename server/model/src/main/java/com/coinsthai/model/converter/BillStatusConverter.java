package com.coinsthai.model.converter;

import com.coinsthai.pojo.intenum.BillStatus;

/**
 * @author
 */
public class BillStatusConverter extends IntEnumConverter<BillStatus> {

    @Override
    public BillStatus convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }

        for (BillStatus intenum : BillStatus.values()) {
            if (intenum.getNumber() == dbData) {
                return intenum;
            }
        }
        return null;
    }
}
