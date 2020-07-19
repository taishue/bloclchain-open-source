package com.coinsthai.blockchain;

import com.coinsthai.pojo.blockchain.CoinType;
import com.coinsthai.vo.wallet.WalletCreateRequest;

/**
 * @author
 */
public interface WalletCreator {

    /**
     * 创建指定币种的钱包地址
     *
     * @param request
     */
    void create(WalletCreateRequest request);

    CoinType getCoinType();
}
