package com.coinsthai.repository.blockchain;

import com.coinsthai.pojo.blockchain.BitcoinTransactionPojo;
import com.coinsthai.repository.AbstractRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

/**
 * @author
 */
@NoRepositoryBean
public interface BitcoinTransactionRepository<T extends BitcoinTransactionPojo> extends AbstractRepository<T> {

    List<T> findByConfirmationsGreaterThanAndConfirmationsLessThan(int min, int max);
}
