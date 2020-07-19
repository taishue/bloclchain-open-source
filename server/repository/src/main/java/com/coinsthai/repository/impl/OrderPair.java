package com.coinsthai.repository.impl;

import com.coinsthai.pojo.common.BasePojo;
import org.springframework.data.domain.Sort;
import org.springframework.util.Assert;

/**
 * @author
 */
public class OrderPair extends BasePojo {

    private String property;

    private Sort.Direction direction;

    public OrderPair(String property, Sort.Direction direction) {
        this.property = property;
        this.direction = direction;
    }

    public String getProperty() {
        return property;
    }

    public Sort.Direction getDirection() {
        if (direction == null) {
            return Sort.Direction.ASC;
        }
        return direction;
    }

    public void setProperty(String property) {
        Assert.notNull(property, "Property can't be null.");
        this.property = property;
    }

    public Sort.Order toSortOrder() {
        return new Sort.Order(direction, property);
    }

    public static OrderPair from(String orderString) {
        String orderProperty = null;
        Sort.Direction direction = null;
        if (orderString.startsWith("-")) {
            direction = Sort.Direction.DESC;
            orderProperty = orderString.substring(1);
        }
        else if (orderString.startsWith("+")) {
            direction = Sort.Direction.ASC;
            orderProperty = orderString.substring(1);
        }
        else {
            direction = Sort.Direction.ASC;
            orderProperty = orderString;
        }
        return new OrderPair(orderProperty, direction);
    }

}
