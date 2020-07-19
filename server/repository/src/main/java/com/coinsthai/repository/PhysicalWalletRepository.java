package com.coinsthai.repository;

import com.coinsthai.model.blockchain.PhysicalWallet;
import com.coinsthai.pojo.blockchain.CoinType;

/**
 * @author
 */
public interface PhysicalWalletRepository extends AbstractRepository<PhysicalWallet> {

    PhysicalWallet findByWalletId(String walletId);

    PhysicalWallet findByAddressAndCoinType(String address, CoinType coinType);
}
