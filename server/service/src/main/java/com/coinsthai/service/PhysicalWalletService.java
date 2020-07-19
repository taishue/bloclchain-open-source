package com.coinsthai.service;

import com.coinsthai.model.Wallet;
import com.coinsthai.model.blockchain.PhysicalWallet;
import com.coinsthai.pojo.blockchain.CoinType;

/**
 * @author
 */
public interface PhysicalWalletService {

    /**
     * 根据钱包创建物理钱包
     *
     * @param wallet
     * @return
     */
    PhysicalWallet create(Wallet wallet);

    PhysicalWallet updateBalance(String walletId, long balance);

    PhysicalWallet updateBalance(String address, CoinType coinType, long balance);

    /**
     * 修改余额
     *
     * @param address
     * @param coinType
     * @param volume   变化的数量，可能为负数
     * @return
     */
    PhysicalWallet increaseBalance(String address, CoinType coinType, long volume);

    PhysicalWallet getByWallet(String walletId);

    PhysicalWallet getByAddressAndType(String address, CoinType type);
}
