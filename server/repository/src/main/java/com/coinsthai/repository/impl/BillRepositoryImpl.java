package com.coinsthai.repository.impl;

import com.coinsthai.model.Bill;
import com.coinsthai.pojo.intenum.BillStatus;
import com.coinsthai.pojo.intenum.BillType;
import com.coinsthai.pojo.parametric.BillParametric;
import com.coinsthai.repository.BillCustomRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author
 */
@Repository
public class BillRepositoryImpl extends CustomRepositoryImpl implements BillCustomRepository {

    private static final String MATCH_PREFIX = "from Bill o where o.market.id=:marketId and o.status=:status and o.type=:type ";

    private static final String EXTRIME_PREIX =
            "select new Bill(o.price, sum(o.remainVolume)) from Bill o where o.market.id=:marketId and o.status=:status and o.type=:type group by o.price ";

    // 根据卖单价格查找买单，买单的价要>=卖单的价
    private static final String BUY_LIMITED =
            MATCH_PREFIX + " and o.price>=:price order by o.price desc, o.createdAt asc";

    private static final String BUY_UNLIMITED = MATCH_PREFIX + " order by o.price asc, o.createdAt asc";

    private static final String BUY_HIGHEST = EXTRIME_PREIX + " order by o.price desc";

    // 根据买单价格查找卖单，卖单的价要<=买单的价
    private static final String SELL_LIMITED =
            MATCH_PREFIX + " and o.price<=:price order by o.price asc, o.createdAt desc";

    private static final String SELL_UNLIMITED = MATCH_PREFIX + " order by o.price asc, o.createdAt asc";

    private static final String SELL_LOWEST = EXTRIME_PREIX + " order by o.price asc";

    @Override
    public Page<Bill> findByPage(BillParametric parametric) {
        Map<String, Object> parameters = new HashMap<>();
        String whereClause = createWhereClause(parametric, parameters);

        String countJpql = "select count(o) from Bill o " + whereClause;
        String listJpql = "from Bill o " + whereClause + " order by o.createdAt desc";
        return queryPage(countJpql, listJpql, parameters, parametric);
    }

    @Override
    public List<Bill> findMatchBuys(String marketId, long price) {
        Map<String, Object> parameters = new HashMap<>();
        String jpql = null;
        if (price == 0l) {
            jpql = BUY_UNLIMITED;
        }
        else {
            jpql = BUY_LIMITED;
            parameters.put("price", price);
        }

        Query query = createQuery(jpql, parameters, marketId, BillType.BUY);
        return query.getResultList();
    }

    @Override
    public List<Bill> findMatchSells(String marketId, long price) {
        Map<String, Object> parameters = new HashMap<>();
        String jpql = null;
        if (price == 0l) {
            jpql = SELL_UNLIMITED;
        }
        else {
            jpql = SELL_LIMITED;
            parameters.put("price", price);
        }

        Query query = createQuery(jpql, parameters, marketId, BillType.SELL);
        return query.getResultList();
    }

    @Override
    public List<Bill> findLowestPendingSells(String marketId) {
        Map<String, Object> parameters = new HashMap<>();
        Query query = createQuery(SELL_LOWEST, parameters, marketId, BillType.SELL);
        return query.getResultList();
    }

    @Override
    public List<Bill> findHighestPendingBuys(String marketId) {
        Map<String, Object> parameters = new HashMap<>();
        Query query = createQuery(BUY_HIGHEST, parameters, marketId, BillType.BUY);
        return query.getResultList();
    }

    private Query createQuery(String jpql, Map<String, Object> parameters, String marketId, BillType type) {
        parameters.put("marketId", marketId);
        parameters.put("type", type);
        parameters.put("status", BillStatus.PENDING);

        Query query = entityManager.createQuery(jpql);
        setParameters(query, parameters);
        query.setFirstResult(0);
        query.setMaxResults(BILL_COUNT_TO_DEAL);
        return query;
    }

    private String createWhereClause(BillParametric parametric, Map<String, Object> params) {
        StringBuilder sb = new StringBuilder(" where 1=1 ");

        if (parametric.getType() != null) {
            sb.append(" and o.type=:type ");
            params.put("type", parametric.getType());
        }

        if (parametric.getStatus() != null) {
            sb.append(" and o.status=:status ");
            params.put("status", parametric.getStatus());
        }
        if (parametric.getFinished() != null) {
            if (parametric.getFinished()) {
                sb.append(" and (o.status=:status1 or o.status=:status2) ");
                params.put("status1", BillStatus.FINISHED);
                params.put("status2", BillStatus.REVOKED_PART);
            }
            else {
                sb.append(" and o.status=:status ");
                params.put("status", BillStatus.PENDING);
            }
        }

        if (StringUtils.isNotBlank(parametric.getUserId())) {
            sb.append(" and o.user.id=:userId ");
            params.put("userId", parametric.getUserId());
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
}
