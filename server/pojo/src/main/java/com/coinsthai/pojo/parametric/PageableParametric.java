package com.coinsthai.pojo.parametric;

import java.util.List;

/**
 * 分页查询参数<br/>
 * Created by 
 */
public interface PageableParametric {
    
    /**
     * 获得页码，从0开始
     * 
     * @return
     */
    int getPage();
    
    /**
     * 设置页码
     * 
     * @param page
     */
    void setPage(int page);
    
    /**
     * 获得每页记录数
     * 
     * @return
     */
    int getSize();
    
    /**
     * 设置每页记录数
     * 
     * @param size
     */
    void setSize(int size);
    
    /**
     * 获得排序方式，+为升序，-为降序<br/>
     * 例如，+createdAt或createdAt为按createdAt升序，-createdAt为按createdAt降序
     * 
     * @return
     */
    List<String> getOrders();
    
    /**
     * 设置排序方式
     * 
     * @param orders
     */
    void setOrders(List<String> orders);
}
