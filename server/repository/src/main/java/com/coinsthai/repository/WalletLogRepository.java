package com.coinsthai.repository;

import com.coinsthai.model.WalletLog;
import com.coinsthai.pojo.intenum.WalletLogType;

/**
 * @author
 */
public interface WalletLogRepository extends AbstractRepository<WalletLog>, WalletLogCustomRepository {

    WalletLog findByWalletIdAndTypeAndBizId(String walletId, WalletLogType type, String bizId);
}
