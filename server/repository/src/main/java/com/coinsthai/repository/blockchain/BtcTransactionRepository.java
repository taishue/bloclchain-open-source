package com.coinsthai.repository.blockchain;

import com.coinsthai.model.blockchain.BtcTransaction;
import com.coinsthai.repository.AbstractRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author YeYifeng
 */
public interface BtcTransactionRepository extends AbstractRepository<BtcTransaction> {

	List<BtcTransaction> findByConfirmed(boolean confirmed);

	@Query("select count(o.txHash) from BtcTransaction o where o.txHash=:txHash")
	Long findCountForTxHash(@Param("txHash") String txHash);
}
