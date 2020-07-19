package com.coinsthai.service;

import com.coinsthai.model.*;
import com.coinsthai.pojo.blockchain.CoinType;
import com.coinsthai.vo.BalanceUpdateRequest;
import com.coinsthai.vo.wallet.WalletLogParameter;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author
 */
public interface WalletService {

    Wallet create(Wallet wallet);

    // 为机器人创建假钱包
    Wallet createFakeWallet(User user, String coinId);

    /**
     * 请求创建钱包
     * <p>
     * 如果已有钱包，则返回钱包
     * 如果初步验证有钱，则抛出异常
     *
     * @param userId
     * @param coinId
     * @return
     */
    Wallet requestCreate(String userId, String coinId);

    /**
     * 请求创建钱包
     *
     * @param userId
     * @param coinId
     * @return 如果有则返回钱包，其他情况返回null，不抛异常
     */
    Wallet requestCreateSilently(String userId, String coinId);

    Wallet updateAvailableBalance(BalanceUpdateRequest request);

    Wallet freezeBalance(BalanceUpdateRequest request);

    Wallet unfreezeBalance(Bill bill);

    Wallet unfreezeBalance(Wallet wallet, Bill bill);

    /**
     * 解冻因为撤销或拒绝提现申请的相关解冻
     * @param withdraw
     * @return
     */
    Wallet unfreezeBalance(Withdraw withdraw);

    /**
     * 在交易中，钱包余额减少的变更
     *
     * @param request
     * @return
     */
    Wallet updateBalanceForDealOut(BalanceUpdateRequest request);

    /**
     * 在交易中，钱包余额增加的变更
     *
     * @param request
     * @return
     */
    Wallet updateBalanceForDealIn(BalanceUpdateRequest request, long brokerage);

    /**
     * 提现，将已冻结的提现申请减去
     * @param withdraw
     * @return
     */
    Wallet updateBalanceForWithdraw(Withdraw withdraw);

    /**
     * 充值至pending balance
     *
     * @param walletId
     * @param volume
     * @param txid
     * @return
     */
    Wallet rechargePendingBalance(String walletId, long volume, String txid);

    /**
     * 充值至available balance
     *
     * @param walletId
     * @param volume
     * @param txid
     * @return
     */
    Wallet rechargeAvailableBalance(String walletId, long volume, String txid);

    /**
     * 充值，将pending balance转至available balance
     *
     * @param walletId
     * @param volume
     * @param txid
     * @return
     */
    Wallet transferPendingBalance(String walletId, long volume, String txid);

    Wallet get(String id);

    Wallet getByUserAndCoin(String userId, String coinId);

    Wallet getByUserAndCoin(String userId, CoinType coinType);

    Wallet getByAddressAndCoin(String address, String coinId);

    Wallet getByAddressAndCoin(String address, CoinType coinType);

    List<Wallet> listByUser(String userId);

    Page<WalletLog> pageLogs(WalletLogParameter parameter);
}
