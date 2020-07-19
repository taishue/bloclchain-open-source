package com.coinsthai.model.converter;

import com.coinsthai.pojo.intenum.Gender;

/**
 * @author
 */
public class GenderConverter extends IntEnumConverter<Gender> {

    @Override
    public Gender convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }

        for (Gender intenum : Gender.values()) {
            if (intenum.getNumber() == dbData) {
                return intenum;
            }
        }
        return null;
    }
}
