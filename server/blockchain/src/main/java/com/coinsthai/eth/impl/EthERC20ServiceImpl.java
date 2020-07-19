package com.coinsthai.eth.impl;

import com.coinsthai.module.audit.AuditLogger;
import com.coinsthai.eth.EthERC20Service;
import com.coinsthai.exception.BizErrorCode;
import com.coinsthai.exception.BizException;
import com.coinsthai.model.Wallet;
import com.coinsthai.model.blockchain.EthERC20Transaction;
import com.coinsthai.repository.blockchain.EthERC20TransactionRepository;
import com.coinsthai.repository.WalletRepository;
import com.coinsthai.service.PhysicalWalletService;
import com.coinsthai.service.WalletService;
import com.coinsthai.util.CoinNumberUtils;
import com.coinsthai.util.NumberConvertUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author YeYifeng
 */
@Service
public class EthERC20ServiceImpl implements EthERC20Service {

	private final static Logger LOGGER = LoggerFactory.getLogger(EthERC20ServiceImpl.class);

	private final static String NO_TRANSACTIONS_FOUND = "No transactions found";

	@Value("${app.eth.query.balance.erc20-api}")
	private String queryERC20BalanceApi;

	@Value("${app.eth.query.transaction.erc20-api}")
	private String queryERC20TransactionApi;

	@Value("${app.eth.transaction.confirmations.standard}")
	private int confirmationsStandard;

	@Value("${app.eth.coin.unit}")
	private String originalUnit;

	@Autowired
	private AuditLogger auditLogger;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private WalletRepository walletRepository;

	@Autowired
	private EthERC20TransactionRepository ethERC20TransactionRepository;

	@Autowired
	private WalletService walletService;

	@Autowired
	private PhysicalWalletService physicalWalletService;

	@Transactional
	@Override
	public void syncBalance(Wallet ethWallet) {
		long maxPendingBlock = getMaxPendingBlock(ethWallet.getAddress());
		long maxConfirmedBlock = getMaxConfirmedBlock(ethWallet.getAddress());

		List<EthERC20Transaction> erc20Transactions = syncTransactions(ethWallet.getAddress());
		if (erc20Transactions != null && !erc20Transactions.isEmpty()) {
			List<Wallet> erc20Wallets = walletRepository.findByCoinTokenOnName(
				ethWallet.getAddress(), ethWallet.getCoin().getName());
			//同步钱包余额
			syncWalletBalance(ethWallet, erc20Transactions, maxPendingBlock, maxConfirmedBlock, erc20Wallets);
			//同步物理钱包余额
			syncPhysicalWalletService(erc20Wallets, erc20Transactions.get(0).getTokenDecimal());
		}
	}

	private void syncWalletBalance(Wallet wallet,
	                               List<EthERC20Transaction> erc20Transactions,
	                               long maxPendingBlock,
	                               long maxConfirmedBlock,
	                               List<Wallet> erc20Wallets) {
		erc20Transactions.forEach(erc20Transaction -> {
			if (wallet.getAddress().toString().equals(erc20Transaction.getToAddress())) {
				// 交易确认数达标的
				if (erc20Transaction.getConfirmations() >= confirmationsStandard) {
					// pending转为available的
					if (erc20Transaction.getBlockNumber() <=  maxPendingBlock) {
						transferPendingBalance(erc20Transaction, erc20Wallets);
					} else {
						// 新拉取到的available的
						if (erc20Transaction.getBlockNumber() > maxConfirmedBlock) {
							// 充值 available balance
							rechargeAvailableBalance(erc20Transaction, erc20Wallets);
						} else {
							// 这种是数据库已经存在的, 不做处理
						}
					}
				} else {
					// 新拉取到的pending的
					if (erc20Transaction.getBlockNumber() >  maxPendingBlock) {
						rechargePendingBalance(erc20Transaction, erc20Wallets);
					} else {
						// 这种是数据库已经存在的, 不做处理
					}
				}
			}
		});
	}

	private void syncPhysicalWalletService(List<Wallet> wallets, int unit) {
		wallets.forEach(wallet -> {
			int localUnit = wallet.getCoin().getUnit();
			BigDecimal originalUnit = CoinNumberUtils.getDecimal(unit);
			String balance = queryBalance(wallet.getCoin().getContract(), wallet.getAddress());
			if (!StringUtils.isEmpty(balance)) {
				physicalWalletService.updateBalance(wallet.getId(),
					NumberConvertUtils.convertToLocal(balance, originalUnit, localUnit));
			}
		});
	}

	private void transferPendingBalance(EthERC20Transaction erc20Transaction, List<Wallet> wallets) {
		Wallet wallet = getMatchedWallet(erc20Transaction.getTokenSymbol(), wallets);
		if (wallet != null) {
			long txValue =  convertTxValueToLocal(wallet, erc20Transaction);
			if (txValue > 0) {
				walletService.transferPendingBalance(wallet.getId(), txValue, erc20Transaction.getTxHash());
			}
		}
	}

	private void rechargeAvailableBalance(EthERC20Transaction erc20Transaction, List<Wallet> wallets) {
		Wallet wallet = getMatchedWallet(erc20Transaction.getTokenSymbol(), wallets);
		if (wallet != null) {
			long txValue =  convertTxValueToLocal(wallet, erc20Transaction);
			if (txValue > 0) {
				walletService.rechargeAvailableBalance(wallet.getId(), txValue, erc20Transaction.getTxHash());
			}
		}
	}

	private void rechargePendingBalance(EthERC20Transaction erc20Transaction, List<Wallet> wallets) {
		Wallet wallet = getMatchedWallet(erc20Transaction.getTokenSymbol(), wallets);
		if (wallet != null) {
			long txValue =  convertTxValueToLocal(wallet, erc20Transaction);
			if (txValue > 0) {
				walletService.rechargePendingBalance(wallet.getId(), txValue, erc20Transaction.getTxHash());
			}
		}
	}

	private long convertTxValueToLocal(Wallet wallet, EthERC20Transaction erc20Transaction) {
		int localUnit = wallet.getCoin().getUnit();
		BigDecimal originalUnit = CoinNumberUtils.getDecimal(erc20Transaction.getTokenDecimal());
		return NumberConvertUtils.convertToLocal(erc20Transaction.getTxValue(), originalUnit, localUnit);
	}

	private Wallet getMatchedWallet(String tokenSymbol, List<Wallet> wallets) {
		for (Wallet wallet : wallets) {
			if (tokenSymbol.equals(wallet.getCoin().getName())) {
				return  wallet;
			}
		}
		return null;
	}

	private long getMaxPendingBlock(String walletAddress) {
		Long maxPendingBlock = ethERC20TransactionRepository.findMaxBlockForPending(walletAddress, confirmationsStandard);
		return maxPendingBlock == null ? 0 : maxPendingBlock;
	}

	private long getMaxConfirmedBlock(String walletAddress) {
		Long maxConfirmedBlock = ethERC20TransactionRepository.findMaxBlockForConfirmed(walletAddress,
			confirmationsStandard);
		return maxConfirmedBlock == null ? 0 : maxConfirmedBlock;
	}

	private List<EthERC20Transaction> syncTransactions(String walletAddress) {
		List<EthERC20TransactionSyncView> transactionsPojos = queryTransactions(walletAddress);
		List<EthERC20Transaction> models = null;
		if (transactionsPojos != null && !transactionsPojos.isEmpty()) {
			models = transactionsPojos.stream()
				.map(pojo -> {
					EthERC20Transaction ethTransaction = new EthERC20Transaction();
					BeanUtils.copyProperties(pojo, ethTransaction);
					ethTransaction.setId(pojo.getTxHash());
					return ethTransaction;
				})
				.collect(Collectors.toList());
			ethERC20TransactionRepository.save(models);
		}
		return models;
	}

	private List<EthERC20TransactionSyncView> queryTransactions(String walletAddress) {
		long startBlock = getStartBlock(walletAddress);
		String queryApi = String.format(queryERC20TransactionApi, walletAddress, startBlock);
		EthERC20TransactionResponse response = restTemplate.getForObject(queryApi, EthERC20TransactionResponse.class);

		if (response.getStatus() == 1) {
			return response.getResult();

		} else if (!NO_TRANSACTIONS_FOUND.equals(response.getMessage())) {
			BizException ex = new BizException(BizErrorCode.WALLET_USER_EMPTY,
				"walletAddress", walletAddress,
				"status", response.getStatus(),
				"message", response.getMessage());
			auditLogger.fail("sync", "ETH sync ERC20 wallet", walletAddress, ex);
		}
		return null;
	}

	private long getStartBlock(String walletAddress) {
		Long pendingMinBlock = ethERC20TransactionRepository.findMinBlockForPending(walletAddress, confirmationsStandard);
		if (pendingMinBlock != null) {
			return pendingMinBlock;
		}
		Long maxBlock = ethERC20TransactionRepository.findMaxBlockForConfirmed(walletAddress, confirmationsStandard);
		return maxBlock == null ? 0 : (maxBlock.longValue() + 1);
	}

	private String queryBalance(String contractAddress, String walletAddress) {
		String queryApi = String.format(queryERC20BalanceApi, contractAddress, walletAddress);
		EthBalanceResponse response = restTemplate.getForObject(queryApi, EthBalanceResponse.class);
		if (response.getStatus() == 1) {
			return response.getResult();
		}
		return null;
	}
}
