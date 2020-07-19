package com.coinsthai.vo;

import com.coinsthai.pojo.common.BasePojo;
import io.swagger.annotations.ApiModel;

/**
 * @author
 */
@ApiModel(description = "K线数据列表外层视图")
public class KlinesWrapper extends BasePojo {

    private Boolean isSuc = true;

    private KlinesView datas;

    public Boolean getSuc() {
        return isSuc;
    }

    public void setSuc(Boolean suc) {
        isSuc = suc;
    }

    public KlinesView getDatas() {
        return datas;
    }

    public void setDatas(KlinesView datas) {
        this.datas = datas;
    }
}
