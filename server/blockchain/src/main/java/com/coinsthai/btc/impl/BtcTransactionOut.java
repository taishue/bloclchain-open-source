package com.coinsthai.btc.impl;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author YeYifeng
 */
public class BtcTransactionOut {

	private boolean spent;

	@JsonProperty("tx_index")
	private long txIndex;

	private int type;

	private String addr;

	private String value;

	private int n;

	private String script;

	public boolean isSpent() {
		return spent;
	}

	public void setSpent(boolean spent) {
		this.spent = spent;
	}

	public long getTxIndex() {
		return txIndex;
	}

	public void setTxIndex(long txIndex) {
		this.txIndex = txIndex;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getN() {
		return n;
	}

	public void setN(int n) {
		this.n = n;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}
}
