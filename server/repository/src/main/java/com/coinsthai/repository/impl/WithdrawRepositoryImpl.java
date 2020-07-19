package com.coinsthai.repository.impl;

import com.coinsthai.model.Withdraw;
import com.coinsthai.pojo.parametric.WithdrawParametric;
import com.coinsthai.repository.WithdrawCustomRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author
 */
@Repository
public class WithdrawRepositoryImpl extends CustomRepositoryImpl implements WithdrawCustomRepository {

    @Override
    public Page<Withdraw> findByPage(WithdrawParametric parametric) {
        Map<String, Object> parameters = new HashMap<>();
        String whereClause = createWhereClause(parametric, parameters);
        String orderClause = createQueryOrder(parametric.getOrders(), "o.createdAt desc");

        String countJpql = "select count(o) from Withdraw o " + whereClause;
        String listJpql = "select o from Withdraw o " + whereClause + orderClause;

        return queryPage(countJpql, listJpql, parameters, parametric);
    }

    private String createWhereClause(WithdrawParametric parametric, Map<String, Object> params) {
        StringBuilder sb = new StringBuilder(" where 1=1 ");

        if (StringUtils.isNotBlank(parametric.getUserId())) {
            sb.append(" and o.user.id=:userId ");
            params.put("userId", parametric.getUserId());
        }

        if (StringUtils.isNotBlank(parametric.getCoinId())) {
            sb.append(" and o.coin.id=:coinId ");
            params.put("coinId", parametric.getCoinId());
        }

        if (parametric.getStatus() != null) {
            sb.append(" and o.status=:status ");
            params.put("status", parametric.getStatus());
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

    private String createQueryOrder(List<String> orderStrings, String defaultOrder) {
        String order = createQueryOrder(orderStrings);
        if (StringUtils.isNotBlank(order) || StringUtils.isBlank(defaultOrder)) {
            return order;
        }

        return " order by " + defaultOrder;
    }

    private String createQueryOrder(List<String> orderStrings) {
        if (orderStrings == null || orderStrings.isEmpty()) {
            return "";
        }

        List<OrderPair> pairs = new ArrayList<>(orderStrings.size());
        for (String orderString : orderStrings) {
            OrderPair pair = OrderPair.from(orderString);
            if (addAliasIfMatch(pair, "createdAt", "o")) {
                pairs.add(pair);
            }
        }

        return createOrderClause(pairs);
    }
}
