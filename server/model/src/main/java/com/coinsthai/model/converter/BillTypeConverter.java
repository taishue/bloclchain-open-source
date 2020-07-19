package com.coinsthai.model.converter;

import com.coinsthai.pojo.intenum.BillType;

/**
 * @author 
 */
public class BillTypeConverter extends IntEnumConverter<BillType> {

    @Override
    public BillType convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }

        for (BillType intenum : BillType.values()) {
            if (intenum.getNumber() == dbData) {
                return intenum;
            }
        }
        return null;
    }
}
