package com.coinsthai.vo;

import com.coinsthai.pojo.common.BasePojo;

import java.util.List;

/**
 * @author 
 */
public class KlinesView extends BasePojo {

    private List<double[]> data;

    public List<double[]> getData() {
        return data;
    }

    public void setData(List<double[]> data) {
        this.data = data;
    }
}
