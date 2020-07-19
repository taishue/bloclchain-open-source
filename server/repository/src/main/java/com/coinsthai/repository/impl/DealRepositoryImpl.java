package com.coinsthai.repository.impl;

import com.coinsthai.model.Deal;
import com.coinsthai.model.Kline;
import com.coinsthai.pojo.parametric.DealParametric;
import com.coinsthai.pojo.parametric.DealSimpleParametric;
import com.coinsthai.repository.DealCustomRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author
 */
@Repository
public class DealRepositoryImpl extends CustomRepositoryImpl implements DealCustomRepository {

    private static final String JPQL_KLINE = "select new com.coinsthai.model.Kline(max(o.price), min(o.price), sum(o.volume)) from Deal o where o.market.id=:marketId and o.createdAt>=:beginDate and o.createdAt<:endDate";

    private static final String JPQL_PRICE = "select o.price from Deal o where o.market.id=:marketId and o.createdAt>=:beginDate and o.createdAt<:endDate ";

    private static final String JPQL_FIRST_PRICE = JPQL_PRICE + " order by o.createdAt asc";

    private static final String JPQL_LAST_PRICE = JPQL_PRICE + " order by o.createdAt desc";

    @Override
    public Page<Deal> findSimpleByPage(DealSimpleParametric parametric) {
        Map<String, Object> parameters = new HashMap<>();
        String whereClause = createSimpleWhereClause(parametric, parameters);

        String countJpql = "select count(o) from Deal o " + whereClause;
        String listJpql = "from Deal o " + whereClause + " order by o.createdAt desc";
        return queryPage(countJpql, listJpql, parameters, parametric);
    }

    @Override
    public Page<Deal> findByPage(DealParametric parametric) {
        Map<String, Object> parameters = new HashMap<>();
        String whereClause = createWhereClause(parametric, parameters);

        String countJpql = "select count(o) from Deal o inner join o.sell s inner join o.buy b " + whereClause;
        String listJpql = "select o from Deal o inner join fetch o.sell s inner join fetch o.buy b " + whereClause +
                " order by o.createdAt desc";
        return queryPage(countJpql, listJpql, parameters, parametric);
    }

    @Override
    public Kline findKline(String marketId, Date beginDate, Date endDate) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("marketId", marketId);
        parameters.put("beginDate", beginDate);
        parameters.put("endDate", endDate);

        Query query = entityManager.createQuery(JPQL_KLINE);
        setParameters(query, parameters);
        query.setFirstResult(0);
        query.setMaxResults(1);

        return (Kline) query.getSingleResult();
    }

    @Override
    public long findFirstPrice(String marketId, Date beginDate, Date endDate) {
        return findPrice(marketId, beginDate, endDate, JPQL_FIRST_PRICE);
    }

    @Override
    public long findLastPrice(String marketId, Date beginDate, Date endDate) {
        return findPrice(marketId, beginDate, endDate, JPQL_LAST_PRICE);
    }

    private long findPrice(String marketId, Date beginDate, Date endDate, String jpql) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("marketId", marketId);
        parameters.put("beginDate", beginDate);
        parameters.put("endDate", endDate);

        Query query = entityManager.createQuery(jpql);
        setParameters(query, parameters);
        query.setFirstResult(0);
        query.setMaxResults(1);
        Long result = (Long) query.getSingleResult();

        if (result == null) {
            return 0l;
        }
        return result;
    }

    private String createSimpleWhereClause(DealSimpleParametric parametric, Map<String, Object> params) {
        StringBuilder sb = new StringBuilder(" where 1=1 ");

        if (parametric.getType() != null) {
            sb.append(" and o.type=:type ");
            params.put("type", parametric.getType());
        }

        if (StringUtils.isNotBlank(parametric.getMarketId())) {
            sb.append(" and o.market.id=:marketId ");
            params.put("marketId", parametric.getMarketId());
        }

        if (parametric.getBeginDate() != null) {
            sb.append(" and o.createdAt>=:beginDate ");
            params.put("beginDate", parametric.getBeginDate());
        }
        if (parametric.getEndDate() != null) {
            sb.append(" and o.createdAt<:endDate ");
            params.put("endDate", parametric.getEndDate());
        }

        return sb.toString();
    }

    private String createWhereClause(DealParametric parametric, Map<String, Object> params) {
        StringBuilder sb = new StringBuilder(createSimpleWhereClause(parametric, params));

        if (StringUtils.isNotBlank(parametric.getSellerId())) {
            sb.append(" and s.user.id=:sellerId ");
            params.put("sellerId", parametric.getSellerId());
        }

        if (StringUtils.isNotBlank(parametric.getBuyerId())) {
            sb.append(" and b.user.id=:buyerId ");
            params.put("buyerId", parametric.getBuyerId());
        }

        if (StringUtils.isNotBlank(parametric.getUserId())) {
            sb.append(" and (b.user.id=:userId or s.user.id=:userId) ");
            params.put("userId", parametric.getUserId());
        }

        return sb.toString();
    }
}
