package com.coinsthai.repository.impl;

import com.coinsthai.model.WalletLog;
import com.coinsthai.pojo.parametric.WalletLogParametric;
import com.coinsthai.repository.WalletLogCustomRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 
 */
@Repository
public class WalletLogRepositoryImpl extends CustomRepositoryImpl implements WalletLogCustomRepository {

    @Override
    public Page<WalletLog> findByPage(WalletLogParametric parametric) {
        Map<String, Object> parameters = new HashMap<>();
        String whereClause = createWhereClause(parametric, parameters);

        String countJpql = "select count(o) from WalletLog o inner join o.wallet w " + whereClause;
        String listJpql = "select o from WalletLog o inner join fetch o.wallet w " + whereClause +
                " order by o.createdAt desc";
        return queryPage(countJpql, listJpql, parameters, parametric);
    }

    private String createWhereClause(WalletLogParametric parametric, Map<String, Object> params) {
        StringBuilder sb = new StringBuilder(" where 1=1 ");

        if (StringUtils.isNotBlank(parametric.getUserId())) {
            sb.append(" and w.user.id=:userId ");
            params.put("userId", parametric.getUserId());
        }

        if (StringUtils.isNotBlank(parametric.getWalletId())) {
            sb.append(" and w.id=:walletId ");
            params.put("walletId", parametric.getWalletId());
        }

        if (parametric.getType() != null) {
            sb.append(" and o.type=:type ");
            params.put("type", parametric.getType());
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
