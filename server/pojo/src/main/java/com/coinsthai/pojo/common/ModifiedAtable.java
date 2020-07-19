package com.coinsthai.pojo.common;

import java.util.Date;

/**
 * Created by
 */
public interface ModifiedAtable extends CreatedAtable {

    Date getModifiedAt();

    void setModifiedAt(Date modifiedAt);
}
