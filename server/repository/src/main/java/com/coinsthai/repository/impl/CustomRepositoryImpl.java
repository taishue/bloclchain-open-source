package com.coinsthai.repository.impl;

import com.coinsthai.pojo.parametric.PageableParametric;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by 
 */
public abstract class CustomRepositoryImpl {

    @Resource
    protected EntityManager entityManager;

    /**
     * 查询记录数
     *
     * @param jpql
     * @param parameters
     * @return
     */
    protected long queryCount(String jpql, Map<String, Object> parameters) {
        Query query = entityManager.createQuery(jpql);
        setParameters(query, parameters);
        return (Long) query.getSingleResult();
    }

    protected Page queryPage(String countJpql,
                             String listJpql,
                             Map<String, Object> parameters,
                             PageableParametric parametric) {
        long count = queryCount(countJpql, parameters);
        if (count == 0) {
            return new PageImpl<>(new ArrayList<>());
        }

        adjustPage(count, parametric);
        Query query = entityManager.createQuery(listJpql);
        setParameters(query, parameters);
        query.setFirstResult(parametric.getPage() * parametric.getSize());
        query.setMaxResults(parametric.getSize());
        List list = query.getResultList();
        PageRequest pageable = new PageRequest(parametric.getPage(),
                                               parametric.getSize());
        return new PageImpl<>(list, pageable, count);
    }

    protected void setParameters(Query query, Map<String, Object> parameters) {
        if (query == null || parameters == null) {
            return;
        }
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
    }

    /**
     * 根据记录总数调整校正页码
     *
     * @param count
     * @param parametric
     */
    protected void adjustPage(long count, PageableParametric parametric) {
        if (count <= 0) {
            parametric.setPage(0);
        }
        else if (count <= parametric.getSize() * parametric.getPage()) {
            int maxPage = (int) count / parametric.getSize();
            if (count % parametric.getSize() == 0) {
                maxPage -= 1;
            }
            parametric.setPage(maxPage);
        }
    }

    protected String createOrderClause(List<OrderPair> list) {
        StringBuilder sb = new StringBuilder();
        if (list == null || list.isEmpty()) {
            return sb.toString();
        }

        sb.append(" order by ");
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }

            OrderPair orderPair = list.get(i);
            sb.append(orderPair.getProperty()).append(" ").append(orderPair.getDirection().name().toLowerCase());
        }

        return sb.toString();
    }

    protected boolean addAliasIfMatch(OrderPair pair, String property, String alias) {
        if (property.equals(pair.getProperty())) {
            pair.setProperty(alias + "." + property);
            return true;
        }
        return false;
    }
}
