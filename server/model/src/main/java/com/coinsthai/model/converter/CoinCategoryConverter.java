package com.coinsthai.model.converter;

import com.coinsthai.pojo.intenum.CoinCategory;

/**
 * @author
 */
public class CoinCategoryConverter extends IntEnumConverter<CoinCategory> {

    @Override
    public CoinCategory convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }

        for (CoinCategory intenum : CoinCategory.values()) {
            if (intenum.getNumber() == dbData) {
                return intenum;
            }
        }
        return null;
    }
}
