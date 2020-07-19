package com.coinsthai.eth.impl;

import com.coinsthai.module.audit.AuditLogger;
import com.coinsthai.eth.CreateWalletResponse;
import com.coinsthai.eth.EthService;
import com.coinsthai.exception.BizErrorCode;
import com.coinsthai.exception.BizException;
import com.coinsthai.model.blockchain.EthTransaction;
import com.coinsthai.model.Wallet;
import com.coinsthai.repository.blockchain.EthTransactionRepository;
import com.coinsthai.service.PhysicalWalletService;
import com.coinsthai.service.WalletService;
import com.coinsthai.util.NumberConvertUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;

import javax.transaction.Transactional;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author YeYifeng
 */
@Service
public class EthServiceImpl implements EthService {

	private final static Logger LOGGER = LoggerFactory.getLogger(EthServiceImpl.class);

	private final static String NO_TRANSACTIONS_FOUND = "No transactions found";

	private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	@Value("${app.eth.wallet.path}")
	private String walletPath;

	@Value("${app.eth.wallet.password}")
	private String walletPassword;

	@Value("${app.eth.query.balance.api}")
	private String queryBalanceApi;

	@Value("${app.eth.query.transaction.api}")
	private String queryTransactionApi;

	@Value("${app.eth.transaction.confirmations.standard}")
	private int confirmationsStandard;

	@Value("${app.eth.coin.unit}")
	private String originalUnit;

	@Autowired
	private AuditLogger auditLogger;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private EthTransactionRepository ethTransactionRepository;

	@Autowired
	private WalletService walletService;

	@Autowired
	private PhysicalWalletService physicalWalletService;

	private Credentials openWallet(String password, String walletPath, String walletName) throws Exception {
		Credentials credentials = WalletUtils.loadCredentials(password, walletPath + "/" + walletName);
		return credentials;
	}

	@Override
	public CreateWalletResponse createWallet() throws Exception {
		String day = dateFormat.format(new Date());
		File file = new File(walletPath + "/" + day);
		if (!file.exists() || !file.isDirectory()) {
			file.mkdir();
		}

		String walletName = WalletUtils.generateFullNewWalletFile(walletPassword, file);
		Credentials credentials = openWallet(walletPassword, file.getPath(), walletName);
		String walletAddress = credentials.getAddress();

		return new CreateWalletResponse(walletAddress, day + "/" + walletName);
	}

	@Transactional
	@Override
	public void syncBalance(Wallet wallet) {
		long maxPendingBlock = getMaxPendingBlock(wallet.getAddress());
		long maxConfirmedBlock = getMaxConfirmedBlock(wallet.getAddress());

		List<EthTransaction> transactions = syncTransactions(wallet.getAddress());
		if (transactions != null && !transactions.isEmpty()) {
			//同步钱包余额
			syncWalletBalance(wallet, transactions, maxPendingBlock, maxConfirmedBlock);
			//同步物理钱包余额
			syncPhysicalWalletService(wallet);
		}
	}

	private void syncWalletBalance(Wallet wallet,
	                               List<EthTransaction> transactions,
	                               long maxPendingBlock,
	                               long maxConfirmedBlock) {
		transactions.forEach(transaction -> {
			long txValue = getLocalTxValue(wallet, transaction);
			if (transaction.getIsError() == 0
				&& wallet.getAddress().toString().equals(transaction.getToAddress())
				&& txValue > 0) {
				// 交易确认数达标的
				if (transaction.getConfirmations() >= confirmationsStandard) {
					// pending转为available的
					if (transaction.getBlockNumber() <=  maxPendingBlock) {
						walletService.transferPendingBalance(wallet.getId(), txValue, transaction.getTxHash());
					} else {
						// 新拉取到的available的
						if (transaction.getBlockNumber() > maxConfirmedBlock) {
							// 充值 available balance
							walletService.rechargeAvailableBalance(wallet.getId(), txValue, transaction.getTxHash());
						} else {
							// 这种是数据库已经存在的, 不做处理
						}
					}
				} else {
					// 新拉取到的pending的
					if (transaction.getBlockNumber() >  maxPendingBlock) {
						walletService.rechargePendingBalance(wallet.getId(), txValue, transaction.getTxHash());
					} else {
						// 这种是数据库已经存在的, 不做处理
					}
				}
			}
		});
	}

	private long getLocalTxValue(Wallet wallet, EthTransaction transaction) {
		int localUnit = wallet.getCoin().getUnit();
		return NumberConvertUtils.convertToLocal(transaction.getTxValue(), originalUnit, localUnit);
	}

	private void syncPhysicalWalletService(Wallet wallet) {
		String balance = queryBalance(wallet.getAddress());
		if (!StringUtils.isEmpty(balance)) {
			int localUnit = wallet.getCoin().getUnit();
			physicalWalletService.updateBalance(wallet.getId(),
				NumberConvertUtils.convertToLocal(balance, originalUnit, localUnit));
		}
	}

	private long getMaxPendingBlock(String walletAddress) {
		Long maxPendingBlock = ethTransactionRepository.findMaxBlockForPending(walletAddress, confirmationsStandard);
		return maxPendingBlock == null ? 0 : maxPendingBlock;
	}

	private long getMaxConfirmedBlock(String walletAddress) {
		Long maxConfirmedBlock = ethTransactionRepository.findMaxBlockForConfirmed(walletAddress,
			confirmationsStandard);
		return maxConfirmedBlock == null ? 0 : maxConfirmedBlock;
	}

	private List<EthTransaction> syncTransactions(String walletAddress) {
		List<EtHTransactionSyncView> transactionsPojos = queryTransactions(walletAddress);
		List<EthTransaction> models = null;
		if (transactionsPojos != null && !transactionsPojos.isEmpty()) {
			models = transactionsPojos.stream()
				.map(pojo -> {
					EthTransaction ethTransaction = new EthTransaction();
					BeanUtils.copyProperties(pojo, ethTransaction);
					ethTransaction.setId(pojo.getTxHash());
					return ethTransaction;
				})
				.collect(Collectors.toList());
			ethTransactionRepository.save(models);
		}
		return models;
	}

	private List<EtHTransactionSyncView> queryTransactions(String walletAddress) {
		long startBlock = getStartBlock(walletAddress);
		String queryApi = String.format(queryTransactionApi, walletAddress, startBlock);
		EthTransactionResponse response = restTemplate.getForObject(queryApi, EthTransactionResponse.class);

		if (response.getStatus() == 1) {
			return response.getResult();
		} else if (!NO_TRANSACTIONS_FOUND.equals(response.getMessage())) {
			BizException ex = new BizException(BizErrorCode.WALLET_USER_EMPTY,
				"walletAddress", walletAddress,
				"status", response.getStatus(),
				"message", response.getMessage());
			auditLogger.fail("sync", "ETH sync wallet", walletAddress, ex);
		}
		return null;
	}

	private long getStartBlock(String walletAddress) {
		Long pendingMinBlock = ethTransactionRepository.findMinBlockForPending(walletAddress, confirmationsStandard);
		if (pendingMinBlock != null) {
			return pendingMinBlock;
		}
		Long maxBlock = ethTransactionRepository.findMaxBlockForConfirmed(walletAddress, confirmationsStandard);
		return maxBlock == null ? 0 : (maxBlock.longValue() + 1);
	}

	private String queryBalance(String walletAddress) {
		String queryApi = String.format(queryBalanceApi, walletAddress);
		EthBalanceResponse response = restTemplate.getForObject(queryApi, EthBalanceResponse.class);
		if (response.getStatus() == 1) {
			return response.getResult();
		}
		return null;
	}
}
