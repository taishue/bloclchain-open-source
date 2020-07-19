package com.coinsthai.model.converter;

import com.coinsthai.pojo.intenum.WalletLogType;

/**
 * @author
 */
public class WalletLogTypeConverter extends IntEnumConverter<WalletLogType> {

    @Override
    public WalletLogType convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }

        for (WalletLogType intenum : WalletLogType.values()) {
            if (intenum.getNumber() == dbData) {
                return intenum;
            }
        }
        return null;
    }
}
