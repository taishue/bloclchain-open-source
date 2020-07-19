package com.coinsthai.eth;

import com.coinsthai.model.Wallet;

/**
 * @author YeYifeng
 */
public interface EthERC20Service {

	void syncBalance(Wallet ethWallet);
}
