package com.coinsthai.btc.impl;

import com.coinsthai.module.audit.AuditLogger;
import com.coinsthai.cache.LatestBlockCache;
import com.coinsthai.btc.BtcService;
import com.coinsthai.btc.LatestBlock;
import com.coinsthai.exception.BizErrorCode;
import com.coinsthai.exception.BizException;
import com.coinsthai.model.*;
import com.coinsthai.model.blockchain.BtcTransaction;
import com.coinsthai.pojo.blockchain.CoinType;
import com.coinsthai.repository.*;
import com.coinsthai.repository.blockchain.BtcTransactionRepository;
import com.coinsthai.service.PhysicalWalletService;
import com.coinsthai.service.WalletService;
import com.coinsthai.util.NumberConvertUtils;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.wallet.DeterministicSeed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.io.File;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author YeYifeng
 */
@Service
public class BtcServiceImpl implements BtcService {

	private final static Logger LOGGER = LoggerFactory.getLogger(BtcServiceImpl.class);

	private final static String LATEST_BLOCK = "LatestBlock";

	private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	@Value("${app.btc.wallet.path}")
	private String walletPath;

	@Value("${app.btc.wallet.password}")
	private String walletPassword;

	@Value("${app.btc.wallet.random.salt}")
	private String randomSalt;

	@Value("${app.btc.query.latest-block}")
	private String queryLatestBlockApi;

	@Value("${app.btc.query.transaction.api}")
	private String queryTransactionApi;

	@Value("${app.btc.query.transaction.by-hash}")
	private String queryTransactionByHash;

	@Value("${app.btc.query.transaction.limit}")
	private int queryLimit;

	@Value("${app.btc.transaction.confirmations.standard}")
	private int confirmationsStandard;

	@Value("${app.btc.coin.unit}")
	private String originalUnit;

	@Autowired
	private AuditLogger auditLogger;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CoinRepository coinRepository;

	@Autowired
	private BtcTransactionRepository btcTransactionRepository;

	@Autowired
	private LatestBlockCache latestBlockCache;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private WalletService walletService;

	@Autowired
	private PhysicalWalletService physicalWalletService;

	@Transactional
	@Override
	public void createWallet(String userId) {
		User user = userRepository.findOne(userId);
		if (user == null) {
			BizException ex = new BizException(BizErrorCode.WALLET_USER_EMPTY, "userId", userId);
			auditLogger.fail("create", "create wallet", userId, ex);
		}

		String day = dateFormat.format(new Date());
		File file = new File(walletPath + "/" + day);
		if (!file.exists() || !file.isDirectory()) {
			file.mkdir();
		}

		String walletName = user.getId() + ".dat";
		try {
			SecureRandom secureRandom  = SecureRandom.getInstance("SHA1PRNG");
			DeterministicSeed deterministicSeed = new DeterministicSeed(secureRandom, 8 * 16,
																		randomSalt,
																		System.currentTimeMillis());

			NetworkParameters params = MainNetParams.get();
			org.bitcoinj.wallet.Wallet wallet = org.bitcoinj.wallet.Wallet.fromSeed(params, deterministicSeed);
			wallet.encrypt(walletPassword);

			wallet.saveToFile(new File(file.getPath() + "/" + walletName));
			Address currentReceiveAddress = wallet.currentReceiveAddress();

			Wallet walletModel = new Wallet();
			walletModel.setUser(user);
			walletModel.setPrivateKey(day + "/" + walletName);
			walletModel.setAddress(currentReceiveAddress.toString());
			walletModel.setCoin(coinRepository.findByName(CoinType.BTC.name()));
			walletService.create(walletModel);
		} catch (Exception e) {
			LOGGER.error("create BTC wallet failed", e);
			BizException ex = new BizException(BizErrorCode.WALLET_USER_EMPTY, "userId", userId, e.getMessage());
			auditLogger.fail("create", "create BTC wallet", userId, ex);
		}
	}

	@Override
	public void syncLatestBlock() {
		LatestBlock latestBlock = restTemplate.getForObject(queryLatestBlockApi, LatestBlock.class);
		latestBlockCache.set(LATEST_BLOCK, latestBlock, 1, TimeUnit.DAYS);
	}

	@Transactional
	@Override
	public void syncBalance(Wallet wallet) {
		LatestBlock latestBlock = getLatestBlock();
		syncPendingTransactions(wallet, latestBlock);

		syncTransactions(wallet, latestBlock, 0, 0);
	}

	private LatestBlock getLatestBlock() {
		LatestBlock latestBlock = latestBlockCache.get(LATEST_BLOCK);
		if (latestBlock == null) {
			syncLatestBlock();
			latestBlock = latestBlockCache.get(LATEST_BLOCK);
		}
		return latestBlock == null ? new LatestBlock() : latestBlock;
	}

	private void syncPendingTransactions(Wallet wallet, LatestBlock latestBlock) {
		List<BtcTransaction> pendingTransactions = btcTransactionRepository.findByConfirmed(false);
		if (pendingTransactions == null || pendingTransactions.isEmpty()) {
			return;
		}
		Map<String, Boolean> map = new HashMap<>();
		for (BtcTransaction transaction : pendingTransactions) {
			if (!map.containsKey(transaction.getTxHash())) {
				BtcTransactionSyncView transactionSyncView = queryTransactionByHash(transaction.getTxHash());
				map.put(transaction.getTxHash(), (transactionSyncView == null ? false : true));
			}
			Boolean transactionExists = map.get(transaction.getTxHash());
			if (transactionExists == null || !transactionExists) {
				continue;
			}
			if (isConfirmed(latestBlock, transaction.getBlockHeight())) {
				long txValue = getLocalTxValue(wallet, transaction);
				if (wallet.getAddress().equals(transaction.getToAddress()) && txValue > 0) {
					walletService.transferPendingBalance(wallet.getId(), txValue, transaction.getTxHash());
				}
				transaction.setConfirmed(true);
				btcTransactionRepository.save(transaction);
			}
		}
	}

	private BtcTransactionSyncView queryTransactionByHash(String txHash) {
		String queryApi = String.format(queryTransactionByHash, txHash);
		try {
			return restTemplate.getForObject(queryApi, BtcTransactionSyncView.class);
		} catch (Exception e) {
			if ( e instanceof HttpServerErrorException) {
				HttpServerErrorException serverErrorException = (HttpServerErrorException) e;
				if (serverErrorException.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
					return null;
				}
			}
			LOGGER.error("BTC queryTransactionByHash failed, txHash:{}", txHash);
			BizException ex = new BizException(BizErrorCode.WALLET_USER_EMPTY, "walletAddress", txHash, e.getMessage());
			auditLogger.fail("sync", "BTC queryTransactionByHash", txHash, ex);
		}
		return null;
	}

	private boolean isConfirmed(LatestBlock latestBlock, int currentBlockHeight) {
		return latestBlock.getHeight() - currentBlockHeight >= confirmationsStandard;
	}

	private void syncTransactions(Wallet wallet, LatestBlock latestBlock, int offset, int count) {
		BtcTransactionResponse response = queryTransactions(wallet.getAddress(), offset);
		if (response ==  null || response.getTxs() == null ||  response.getTxs().isEmpty()) {
			return;
		}
		List<BtcTransaction> transactions = new ArrayList<>();
		boolean hasDuplicateTx = saveTransactions(response, wallet, latestBlock, transactions);
		count = count + response.getTxs().size();

		syncWalletBalance(transactions, wallet);

		if (!hasDuplicateTx && response.getNtx() > count) {
			syncTransactions(wallet, latestBlock, offset + queryLimit, count);
		} else {
			int localUnit = wallet.getCoin().getUnit();
			physicalWalletService.updateBalance(wallet.getId(),
				NumberConvertUtils.convertToLocal(response.getFinalBalance(), originalUnit, localUnit));
		}
	}

	private BtcTransactionResponse queryTransactions(String walletAddress, int offset) {
		String queryApi = String.format(queryTransactionApi, walletAddress, queryLimit, offset);
		try {
			return restTemplate.getForObject(queryApi, BtcTransactionResponse.class);
		} catch (Exception e) {
			if ( e instanceof HttpServerErrorException) {
				HttpServerErrorException serverErrorException = (HttpServerErrorException) e;
				if (serverErrorException.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
					return null;
				}
			}
			LOGGER.error("BTC queryTransactions failed:", e);
			BizException ex = new BizException(BizErrorCode.WALLET_USER_EMPTY, "walletAddress", walletAddress, e.getMessage());
			auditLogger.fail("sync", "BTC queryTransactions", walletAddress, ex);
		}
		return null;
	}

	private boolean saveTransactions(BtcTransactionResponse response,
	                                              Wallet wallet,
	                                              LatestBlock latestBlock,
	                                              List<BtcTransaction> models) {
		List<BtcTransactionSyncView> txs = response.getTxs();
		boolean hasDuplicateTx = false;
		for (BtcTransactionSyncView tx : txs) {
			Long count = btcTransactionRepository.findCountForTxHash(tx.getTxHash());
			if (count != null && count > 0) {
				hasDuplicateTx = true;
				break;
			}
			if (tx.getInputs() != null && !tx.getOut().isEmpty()) {
				tx.getInputs().forEach(txInput -> {
					if (txInput.getPrevOut() != null && wallet.getAddress().equals(txInput.getPrevOut().getAddr())) {
						BtcTransaction btcTransaction = new BtcTransaction();
						BeanUtils.copyProperties(tx, btcTransaction);
						btcTransaction.setWallet(wallet);
						btcTransaction.setFromAddress(wallet.getAddress());
						btcTransaction.setToAddress(txInput.getPrevOut().getAddr());
						btcTransaction.setTxValue(txInput.getPrevOut().getValue());
						btcTransaction.setConfirmed(isConfirmed(latestBlock, tx.getBlockHeight()));
						models.add(btcTransaction);
					}
				});
			}
			if (tx.getOut() != null && !tx.getOut().isEmpty()) {
				tx.getOut().forEach(out -> {
					if (wallet.getAddress().equals(out.getAddr())) {
						BtcTransaction btcTransaction = new BtcTransaction();
						BeanUtils.copyProperties(tx, btcTransaction);
						btcTransaction.setWallet(wallet);
						btcTransaction.setToAddress(wallet.getAddress());
						btcTransaction.setTxValue(out.getValue());
						btcTransaction.setConfirmed(isConfirmed(latestBlock, tx.getBlockHeight()));
						models.add(btcTransaction);
					}
				});
			}
		}
		btcTransactionRepository.save(models);
		return hasDuplicateTx;
	}

	private void syncWalletBalance(List<BtcTransaction> transactions, Wallet wallet) {
		for (BtcTransaction transaction : transactions) {
			long txValue = getLocalTxValue(wallet, transaction);
			if (!wallet.getAddress().equals(transaction.getToAddress()) || txValue <= 0) {
				continue;
			}
			if (transaction.isConfirmed()) {
				walletService.rechargeAvailableBalance(wallet.getId(), txValue, transaction.getTxHash());
			} else {
				walletService.rechargePendingBalance(wallet.getId(),
					txValue,
					transaction.getTxHash());
			}
		}
	}

	private long getLocalTxValue(Wallet wallet, BtcTransaction transaction) {
		int localUnit = wallet.getCoin().getUnit();
		return NumberConvertUtils.convertToLocal(transaction.getTxValue(), originalUnit, localUnit);
	}
}
