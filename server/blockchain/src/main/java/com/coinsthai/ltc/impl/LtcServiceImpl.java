package com.coinsthai.ltc.impl;

import com.coinsthai.bitcoin.BitcoinRpcConfigProvider;
import com.coinsthai.bitcoin.BitcoinServiceImpl;
import com.coinsthai.ltc.LtcService;
import com.coinsthai.model.blockchain.LtcTransaction;
import com.coinsthai.model.blockchain.LtcTransactionItem;
import com.coinsthai.pojo.blockchain.CoinType;
import com.coinsthai.repository.blockchain.BitcoinTransactionItemRepository;
import com.coinsthai.repository.blockchain.BitcoinTransactionRepository;
import com.coinsthai.repository.blockchain.LtcTransactionItemRepository;
import com.coinsthai.repository.blockchain.LtcTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author
 */
@Service
public class LtcServiceImpl extends BitcoinServiceImpl<LtcTransaction, LtcTransactionItem> implements LtcService {

    @Autowired
    private LtcConfig ltcConfig;

    @Autowired
    private LtcTransactionRepository transactionRepository;

    @Autowired
    private LtcTransactionItemRepository itemRepository;

    @Override
    protected BitcoinTransactionRepository<LtcTransaction> getTransactionRepository() {
        return transactionRepository;
    }

    @Override
    protected BitcoinTransactionItemRepository<LtcTransactionItem> getItemRepository() {
        return itemRepository;
    }

    @Override
    protected BitcoinRpcConfigProvider getRpcConfig() {
        return ltcConfig;
    }

    @Override
    protected LtcTransaction newTransaction() {
        return new LtcTransaction();
    }

    @Override
    protected LtcTransactionItem newTransactionItem() {
        return new LtcTransactionItem();
    }

    @Override
    protected CoinType getCoinType() {
        return CoinType.LTC;
    }
}
