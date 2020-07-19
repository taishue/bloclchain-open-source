package com.coinsthai.btc;

import com.coinsthai.model.User;
import com.coinsthai.model.Wallet;

/**
 * @author YeYifeng
 */
public interface BtcService {

	void createWallet(String userId);

	void syncLatestBlock();

	void syncBalance(Wallet wallet);

}
