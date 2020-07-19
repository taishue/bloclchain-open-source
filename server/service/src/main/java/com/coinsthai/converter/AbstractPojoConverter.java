package com.coinsthai.converter;

import org.springframework.data.domain.Page;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by 
 */
public abstract class AbstractPojoConverter<T, S>
                                           implements PojoConverter<T, S> {
    
    /**
     * 创建空的目标实例，子类可覆盖
     *
     * @return
     */
    protected T createEmptyTarget(S source) {
        Class<T> type =
                      (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        try {
            return type.newInstance();
        }
        catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
    
    @Override
    public T toPojo(S source) {
        if (source == null) {
            return null;
        }
        
        T target = createEmptyTarget(source);
        toPojo(source, target);
        
        return target;
    }
    
    @Override
    public List<T> toList(List<S> sourceList) {
        if (sourceList == null) {
            return Collections.emptyList();
        }
        
        List<T> targetList = new ArrayList<>(sourceList.size());
        sourceList.forEach(source -> targetList.add(toPojo(source)));
        return targetList;
    }
    
    @Override
    public Page<T> toPage(Page<S> sourcePage) {
        if (sourcePage == null) {
            return null;
        }
        
        List<T> targetList = toList(sourcePage.getContent());
        Page<T> targetPage = new ConvertedPage<>(sourcePage, targetList);
        return targetPage;
    }
    
}
