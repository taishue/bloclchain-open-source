package com.coinsthai.bch.impl;

import com.coinsthai.bch.BchService;
import com.coinsthai.bitcoin.BitcoinRpcConfigProvider;
import com.coinsthai.bitcoin.BitcoinServiceImpl;
import com.coinsthai.model.blockchain.BchTransaction;
import com.coinsthai.model.blockchain.BchTransactionItem;
import com.coinsthai.pojo.blockchain.CoinType;
import com.coinsthai.repository.blockchain.BchTransactionItemRepository;
import com.coinsthai.repository.blockchain.BchTransactionRepository;
import com.coinsthai.repository.blockchain.BitcoinTransactionItemRepository;
import com.coinsthai.repository.blockchain.BitcoinTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author
 */
@Service
public class BchServiceImpl extends BitcoinServiceImpl<BchTransaction, BchTransactionItem> implements BchService {

    @Autowired
    private BchConfig bchConfig;

    @Autowired
    private BchTransactionRepository transactionRepository;

    @Autowired
    private BchTransactionItemRepository itemRepository;

    @Override
    protected BitcoinTransactionRepository<BchTransaction> getTransactionRepository() {
        return transactionRepository;
    }

    @Override
    protected BitcoinTransactionItemRepository<BchTransactionItem> getItemRepository() {
        return itemRepository;
    }

    @Override
    protected BitcoinRpcConfigProvider getRpcConfig() {
        return bchConfig;
    }

    @Override
    protected BchTransaction newTransaction() {
        return new BchTransaction();
    }

    @Override
    protected BchTransactionItem newTransactionItem() {
        return new BchTransactionItem();
    }

    @Override
    protected CoinType getCoinType() {
        return CoinType.BCH;
    }
}
