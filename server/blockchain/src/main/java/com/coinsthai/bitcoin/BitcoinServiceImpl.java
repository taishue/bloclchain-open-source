package com.coinsthai.bitcoin;

import com.coinsthai.bitcoin.rpc.BitcoinJsonRpcClient;
import com.coinsthai.bitcoin.rpc.BitcoinRpcClient;
import com.coinsthai.exception.BizErrorCode;
import com.coinsthai.exception.BizException;
import com.coinsthai.exception.SystemException;
import com.coinsthai.model.Wallet;
import com.coinsthai.module.audit.AuditLogger;
import com.coinsthai.pojo.blockchain.BitcoinTransactionItemPojo;
import com.coinsthai.pojo.blockchain.BitcoinTransactionPojo;
import com.coinsthai.pojo.blockchain.CoinType;
import com.coinsthai.repository.blockchain.BitcoinTransactionItemRepository;
import com.coinsthai.repository.blockchain.BitcoinTransactionRepository;
import com.coinsthai.service.PhysicalWalletService;
import com.coinsthai.service.WalletService;
import com.coinsthai.util.CoinNumberUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import wf.bitcoin.javabitcoindrpcclient.BitcoindRpcClient;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author
 */
public abstract class BitcoinServiceImpl<T extends BitcoinTransactionPojo, I extends BitcoinTransactionItemPojo>
        implements BitcoinService<T, I>, InitializingBean {

    @Autowired
    private WalletService walletService;

    @Autowired
    private PhysicalWalletService physicalWalletService;

    @Autowired
    private AuditLogger auditLogger;

    private BitcoinRpcClient rpcClient;

    protected abstract BitcoinTransactionRepository<T> getTransactionRepository();

    protected abstract BitcoinTransactionItemRepository<I> getItemRepository();

    protected abstract BitcoinRpcConfigProvider getRpcConfig();

    protected abstract T newTransaction();

    protected abstract I newTransactionItem();

    protected abstract CoinType getCoinType();

    @Override
    public void uploadAddress(String userId, String address) {
        rpcClient.importAddress(address, userId, false);
    }

    @Transactional
    @Override
    public void fetchTransactions() {
        int skip = 0;

        List<BitcoindRpcClient.Transaction> unsavedList = new ArrayList<>();
        do {
            List<BitcoindRpcClient.Transaction> list = rpcClient.listTransactions("*",
                                                                                  getRpcConfig().getQueryLimit(),
                                                                                  skip,
                                                                                  true);
            if (list.isEmpty()) {
                break;
            }

            int latestSavedIndex = getLatestSavedIndex(list);
            if (latestSavedIndex == 0) {
                break;  //全部已保存
            }
            if (latestSavedIndex < 0) {
                unsavedList.addAll(list);   // 全部未保存
            }
            else {
                unsavedList.addAll(list.subList(0, latestSavedIndex - 1));
                break;  //有部分保存
            }

            if (list.size() < getRpcConfig().getQueryLimit()) {
                break;  //已到最后一页
            }

            skip += getRpcConfig().getQueryLimit();
        } while (true);

        for (int i = unsavedList.size() - 1; i >= 0; i--) {
            saveTransaction(unsavedList.get(i));
        }

    }

    @Override
    public List<T> listTransactionsByConfirmations(int minConfirmations, int maxConfirmations) {
        return getTransactionRepository().findByConfirmationsGreaterThanAndConfirmationsLessThan(minConfirmations,
                                                                                                 maxConfirmations);
    }

    @Transactional
    @Override
    public void confirmTransaction(T transaction) {
        if (transaction == null || transaction.getConfirmations() < getRpcConfig().getMinConfirmations()) {
            return;
        }

        List<I> items = getItemRepository().findByTxid(transaction.getId());
        items.forEach(item -> confirmTransactionItem(item));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        rpcClient = new BitcoinJsonRpcClient(new URL(getRpcConfig().getRpcUrl()));
    }

    private void confirmTransactionItem(I item) {
        Wallet wallet = walletService.getByAddressAndCoin(item.getAddress(), getCoinType());
        if (wallet == null) {
            BizException ex = new BizException(BizErrorCode.WALLET_NOT_FOUND,
                                               "address,coin",
                                               item.getAddress(),
                                               getCoinType());
            auditLogger.fail("confirm", "bitcoin transaction item", item.getId(), ex);
        }

        long volume = item.getAmount()
                          .multiply(new BigDecimal(getRpcConfig().getCoinUnit()))
                          .setScale(0, BigDecimal.ROUND_HALF_UP)
                          .longValue();
        if (BitcoinTransactionItemPojo.CATEGORY_RECEIVE.equalsIgnoreCase(item.getCategory())) {
            walletService.transferPendingBalance(wallet.getId(), volume, item.getTxid());
            physicalWalletService.increaseBalance(item.getAddress(), getCoinType(), volume);
        }
        else if (BitcoinTransactionItemPojo.CATEGORY_SEND.equalsIgnoreCase(item.getCategory())) {
            physicalWalletService.increaseBalance(item.getAddress(), getCoinType(), volume);
        }
    }

    private void saveTransaction(BitcoindRpcClient.Transaction srcTx) {
        Wallet wallet = walletService.getByAddressAndCoin(srcTx.address(), getCoinType());
        if (wallet == null) {
            return; // 不是交易所的钱包
        }

        I item = getItemRepository().findByAddressAndTxid(srcTx.address(), srcTx.txId());
        if (item != null) {
            return;
        }

        item = newTransactionItem();
        item.setAddress(srcTx.address());
        item.setTxid(srcTx.txId());
        item.setAmount(new BigDecimal(String.valueOf(srcTx.amount())));
        item.setFee(new BigDecimal(String.valueOf(srcTx.fee())));
        item.setCategory(srcTx.category());
        getItemRepository().save(item);

        T transaction = getTransactionRepository().findOne(srcTx.txId());
        if (transaction == null) {
            transaction = newTransaction();
        }
        transaction.setId(srcTx.txId());
        transaction.setBlockHash(srcTx.blockHash());
        transaction.setBlockIndex(srcTx.blockIndex());
        transaction.setBlockTime(srcTx.blockTime());
        transaction.setConfirmations(srcTx.confirmations());
        transaction.setTime(srcTx.time());
        transaction.setTimeReceived(srcTx.timeReceived());
        getTransactionRepository().save(transaction);

        if (BitcoinTransactionItemPojo.CATEGORY_RECEIVE.equalsIgnoreCase(item.getCategory())) {
            long volume = CoinNumberUtils.parseLong(srcTx.amount(), getRpcConfig().getCoinUnit());
            // 充值
            if (transaction.getConfirmations() >= getRpcConfig().getMinConfirmations()) {
                walletService.rechargeAvailableBalance(wallet.getId(), volume, transaction.getId());
                physicalWalletService.increaseBalance(item.getAddress(), getCoinType(), volume);
            }
            else {
                walletService.rechargePendingBalance(wallet.getId(), volume, transaction.getId());
            }
        }
        else if (BitcoinTransactionItemPojo.CATEGORY_SEND.equalsIgnoreCase(item.getCategory()) &&
                transaction.getConfirmations() >= getRpcConfig().getMinConfirmations()) {
            long volume = item.getAmount()
                              .multiply(new BigDecimal(getRpcConfig().getCoinUnit()))
                              .setScale(0, BigDecimal.ROUND_HALF_UP)
                              .longValue();
            physicalWalletService.increaseBalance(item.getAddress(), getCoinType(), volume);
        }
    }

    // checkingList是按时间由新到旧排列的
    private int getLatestSavedIndex(List<BitcoindRpcClient.Transaction> checkingList) {
        BitcoindRpcClient.Transaction firstTx = checkingList.get(0);
        if (getItemRepository().findByAddressAndTxid(firstTx.address(), firstTx.txId()) != null) {
            return 0;   // 最新的一个都已保存
        }
        if (checkingList.size() == 1) {
            return -1;
        }

        BitcoindRpcClient.Transaction lastTx = checkingList.get(checkingList.size() - 1);
        if (getItemRepository().findByAddressAndTxid(lastTx.address(), lastTx.txId()) == null) {
            return -1;  // 最旧的一个都未保存
        }
        if (checkingList.size() == 2) {
            return 1;
        }

        for (int i = checkingList.size() - 2; i >= 1; i--) {
            BitcoindRpcClient.Transaction tx = checkingList.get(i);
            if (getItemRepository().findByAddressAndTxid(tx.address(), tx.txId()) != null) {
                return i;
            }
        }

        throw new SystemException(SystemException.TYPE.UNKNOWN);   // 不应该执行到这里
    }

}
