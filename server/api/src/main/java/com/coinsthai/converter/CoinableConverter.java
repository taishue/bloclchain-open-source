package com.coinsthai.converter;

import com.coinsthai.service.CoinService;
import com.coinsthai.vo.CoinView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @author
 */
public abstract class CoinableConverter<T, S> extends BeanCopyPojoConverter<T, S> {

    @Autowired
    private CoinService coinService;

    @Override
    protected void afterBeanCopy(S source, T target) {
        super.afterBeanCopy(source, target);

        CoinView coin = coinService.get(resolveCoinId(source));
        if (coin == null) {
            return;
        }
        convertSpecials(source, target, coin);
    }

    /**
     * 转换列表，仅适合列表中的元素含有同一个币种
     *
     * @param sourceList
     * @return
     */
    @Override
    public List<T> toList(List<S> sourceList) {
        if (sourceList == null || sourceList.isEmpty()) {
            return super.toList(sourceList);
        }

        CoinView coin = coinService.get(resolveCoinId(sourceList.get(0)));
        if (coin == null) {
            return super.toList(sourceList);
        }

        List<T> targetList = new ArrayList<>(sourceList.size());
        sourceList.forEach(source -> {
            T target = createEmptyTarget(source);
            toPojo(source, target, coin);
            targetList.add(target);
        });

        return targetList;
    }

    public void toPojo(S source, T target, CoinView coin) {
        if (source == null || target == null) {
            return;
        }

        BeanUtils.copyProperties(source, target, ignoreFieldsWhileCreating(target));
        convertSpecials(source, target, coin);
    }

    /**
     * 获得CoinId
     *
     * @param source
     * @return
     */
    protected abstract String resolveCoinId(S source);

    /**
     * 转换币种相关的数值及其他不能直接拷贝的属性
     *
     * @param source
     * @param target
     * @param coin
     */
    protected abstract void convertSpecials(S source, T target, CoinView coin);
}
