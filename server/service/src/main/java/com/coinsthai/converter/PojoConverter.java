package com.coinsthai.converter;

import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by
 */
public interface PojoConverter<T, S> {
    
    /**
     * 将源对象转换为目标对象
     * 
     * @param source
     *            源对象
     * @return 转换后的目标对象
     */
    T toPojo(S source);
    
    /**
     * 将源对象的属性值复制到目标对象中
     * 
     * @param source
     *            源对象
     * @param target
     *            目标对象
     */
    void toPojo(S source, T target);

    /**
     * 将源对象列表转换为目标对象列表
     * 
     * @param sourceList
     *            源对象列表
     * @return 目标对象列表
     */
    List<T> toList(List<S> sourceList);
    
    /**
     * 将源对象分页信息转换为目标对象分页信息
     * 
     * @param sourcePage
     *            源对象分页信息
     * @return 目标对象分页信息
     */
    Page<T> toPage(Page<S> sourcePage);
}
