package com.coinsthai.model;

import com.coinsthai.pojo.common.CreatedAtable;
import com.coinsthai.pojo.common.ModifiedAtable;

import java.util.Date;

/**
 * @author
 */
public class ModelUtils {

    public static void prePersist(ModifiedAtable model) {
        if (model.getCreatedAt() == null) {
            model.setCreatedAt(new Date());
        }
        if (model.getModifiedAt() == null) {
            model.setModifiedAt(model.getCreatedAt());
        }
    }

    public static void prePersist(CreatedAtable model) {
        if (model.getCreatedAt() == null) {
            model.setCreatedAt(new Date());
        }
    }

    public static void preUpdate(ModifiedAtable model) {
        model.setModifiedAt(new Date());
    }

}
