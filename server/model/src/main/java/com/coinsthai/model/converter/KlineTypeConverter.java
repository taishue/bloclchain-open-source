package com.coinsthai.model.converter;

import com.coinsthai.pojo.intenum.KlineType;

/**
 * @author
 */
public class KlineTypeConverter extends IntEnumConverter<KlineType> {

    @Override
    public KlineType convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }

        for (KlineType intenum : KlineType.values()) {
            if (intenum.getNumber() == dbData) {
                return intenum;
            }
        }
        return null;
    }
}
