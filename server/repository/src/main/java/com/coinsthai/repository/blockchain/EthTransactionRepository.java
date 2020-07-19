package com.coinsthai.repository.blockchain;

import com.coinsthai.model.blockchain.EthTransaction;
import com.coinsthai.repository.AbstractRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author YeYifeng
 */
public interface EthTransactionRepository extends AbstractRepository<EthTransaction> {

	List<EthTransaction> findByToAddress(String toAddress);

	@Query("select min(o.blockNumber) from EthTransaction o where (o.toAddress=:address or o.fromAddress=:address) and o.confirmations<:standard")
	Long findMinBlockForPending(@Param("address") String address, @Param("standard") int standard);

	@Query("select max(o.blockNumber) from EthTransaction o where (o.toAddress=:address or o.fromAddress=:address) and o.confirmations<:standard")
	Long findMaxBlockForPending(@Param("address") String address, @Param("standard") int standard);

	@Query("select max(o.blockNumber) from EthTransaction o where (o.toAddress=:address or o.fromAddress=:address) and o.confirmations>=:standard")
	Long findMaxBlockForConfirmed(@Param("address") String address, @Param("standard") int standard);
}
