package com.coinsthai.eth;

import com.coinsthai.model.User;
import com.coinsthai.model.Wallet;

/**
 * @author YeYifeng
 */
public interface EthService {

	CreateWalletResponse createWallet() throws Exception;

	void syncBalance(Wallet wallet);
}
