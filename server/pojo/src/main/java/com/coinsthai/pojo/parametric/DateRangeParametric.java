package com.coinsthai.pojo.parametric;

import java.util.Date;

/**
 * Created by 
 */
public interface DateRangeParametric extends PageableParametric{
    
    Date getBeginDate();
    
    Date getEndDate();
}
