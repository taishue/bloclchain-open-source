package com.coinsthai.bitcoin;

import com.coinsthai.pojo.blockchain.BitcoinTransactionItemPojo;
import com.coinsthai.pojo.blockchain.BitcoinTransactionPojo;

import java.util.List;

/**
 * @author
 */
public interface BitcoinService<T extends BitcoinTransactionPojo, I extends BitcoinTransactionItemPojo> {

    void uploadAddress(String userId, String address);

    /**
     * 从全节点获取最新（未记录的）的交易，并保存，同时更新相关钱包的充值及余额
     */
    void fetchTransactions();

    List<T> listTransactionsByConfirmations(int minConfirmations, int maxConfirmations);

    void confirmTransaction(T transaction);
}
