package com.coinsthai.model.converter;

import com.coinsthai.pojo.intenum.IntEnum;

import javax.persistence.AttributeConverter;

/**
 * @author 
 */
public abstract class IntEnumConverter<T extends IntEnum> implements AttributeConverter<T, Integer> {

    @Override
    public Integer convertToDatabaseColumn(T attribute) {
        return attribute.getNumber();
    }

}
