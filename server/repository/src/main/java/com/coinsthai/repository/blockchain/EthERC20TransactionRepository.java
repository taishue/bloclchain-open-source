package com.coinsthai.repository.blockchain;


import com.coinsthai.model.blockchain.EthERC20Transaction;
import com.coinsthai.repository.AbstractRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author YeYifeng
 */
public interface EthERC20TransactionRepository extends AbstractRepository<EthERC20Transaction> {

	List<EthERC20Transaction> findByToAddress(String toAddress);

	@Query("select min(o.blockNumber) from EthERC20Transaction o where (o.toAddress=:address or o.fromAddress=:address) and o.confirmations<:standard")
	Long findMinBlockForPending(@Param("address") String address, @Param("standard") int standard);

	@Query("select max(o.blockNumber) from EthERC20Transaction o where (o.toAddress=:address or o.fromAddress=:address) and o.confirmations<:standard")
	Long findMaxBlockForPending(@Param("address") String address, @Param("standard") int standard);

	@Query("select max(o.blockNumber) from EthERC20Transaction o where (o.toAddress=:address or o.fromAddress=:address) and o.confirmations>=:standard")
	Long findMaxBlockForConfirmed(@Param("address") String address, @Param("standard") int standard);
}
