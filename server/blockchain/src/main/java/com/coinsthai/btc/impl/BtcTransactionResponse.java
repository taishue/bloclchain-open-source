package com.coinsthai.btc.impl;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @author YeYifeng
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BtcTransactionResponse {

	@JsonProperty("n_tx")
	private int ntx;

	@JsonProperty("final_balance")
	private String finalBalance;

	private List<BtcTransactionSyncView> txs;

	public int getNtx() {
		return ntx;
	}

	public String getFinalBalance() {
		return finalBalance;
	}

	public void setFinalBalance(String finalBalance) {
		this.finalBalance = finalBalance;
	}

	public void setNtx(int ntx) {
		this.ntx = ntx;
	}

	public List<BtcTransactionSyncView> getTxs() {
		return txs;
	}

	public void setTxs(List<BtcTransactionSyncView> txs) {
		this.txs = txs;
	}
}
