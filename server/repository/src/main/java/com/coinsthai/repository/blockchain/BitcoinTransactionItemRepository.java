package com.coinsthai.repository.blockchain;

import com.coinsthai.pojo.blockchain.BitcoinTransactionItemPojo;
import com.coinsthai.repository.AbstractRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

/**
 * @author 
 */
@NoRepositoryBean
public interface BitcoinTransactionItemRepository<T extends BitcoinTransactionItemPojo> extends AbstractRepository<T> {

    T findByAddressAndTxid(String address, String txid);

    List<T> findByTxid(String txid);
}
