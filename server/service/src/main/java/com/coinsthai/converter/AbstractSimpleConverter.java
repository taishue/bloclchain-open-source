package com.coinsthai.converter;

import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author
 */
public abstract class AbstractSimpleConverter<T, S> extends BeanCopyPojoConverter<T, S> {

    @Deprecated
    @Override
    public T toPojo(S source) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    @Override
    public List<T> toList(List<S> sourceList) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    @Override
    public Page<T> toPage(Page<S> sourcePage) {
        throw new UnsupportedOperationException();
    }

    /**
     * 转换分页对象，目标列表的数量必须要与分页里源对象数量一致
     *
     * @param targetList
     * @param sourcePage
     * @return
     */
    public Page<T> toPage(Page<S> sourcePage, List<T> targetList) {
        if (sourcePage == null || sourcePage == null) {
            return null;
        }
        if (targetList.size() != sourcePage.getContent().size()) {
            throw new IllegalArgumentException();
        }

        for (int i = 0; i < targetList.size(); i++) {
            toPojo(sourcePage.getContent().get(i), targetList.get(i));
        }

        Page<T> targetPage = new ConvertedPage<>(sourcePage, targetList);
        return targetPage;
    }
}
