package com.coinsthai.vo;

import com.coinsthai.pojo.parametric.PageableParametric;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 
 */
@ApiModel(description = "分页查询参数")
public class PageParameter implements PageableParametric {

    public static final int DEFAULT_SIZE = 20;

    private int page = 0;

    private int size = DEFAULT_SIZE;

    private List<String> orders;

    @ApiModelProperty(value = "页码，从0开始，默认为0")
    @Override
    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    @ApiModelProperty(value = "每页记录数，默认为20")
    @Override
    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @ApiModelProperty(value = "排序方式，由+或-连接字段名组成", hidden = true)
    @Override
    public List<String> getOrders() {
        if (orders == null) {
            orders = new ArrayList<>();
        }
        return orders;
    }

    @Override
    public void setOrders(List<String> orders) {
        this.orders = orders;
    }
}
