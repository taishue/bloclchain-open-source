package com.coinsthai.vo;

import com.coinsthai.pojo.parametric.DateRangeParametric;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * Created by
 */
public class DateRangePageParameter extends PageParameter
        implements DateRangeParametric {

    private Date beginDate;

    private Date endDate;

    @ApiModelProperty(value = "开始时间")
    @Override
    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    @ApiModelProperty(value = "结束时间")
    @Override
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
