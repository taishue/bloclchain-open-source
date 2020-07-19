package com.coinsthai.repository;

import com.coinsthai.model.Market;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author
 */
public interface MarketRepository extends AbstractRepository<Market> {

    @Query("from Market where (target.id=:targetId and base.id=:baseId) or (target.id=:baseId and base.id=:targetId)")
    Market findByCoins(@Param("targetId") String targetId, @Param("baseId") String baseId);

    Market findByTargetIdAndBaseId(String targetId, String baseId);

    List<Market> findAllByOrderByBasePriorityAscTargetPriorityAsc();

    List<Market> findByBaseIdOrderByTargetPriorityAsc(String baseId);
}
