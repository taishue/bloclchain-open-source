package com.coinsthai.btc.impl;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author YeYifeng
 */
public class BtcTransactionInput {

	private long sequence;

	private String witness;

	@JsonProperty("prev_out")
	private BtcTransactionOut prevOut;

	private String script;

	public long getSequence() {
		return sequence;
	}

	public void setSequence(long sequence) {
		this.sequence = sequence;
	}

	public String getWitness() {
		return witness;
	}

	public void setWitness(String witness) {
		this.witness = witness;
	}

	public BtcTransactionOut getPrevOut() {
		return prevOut;
	}

	public void setPrevOut(BtcTransactionOut prevOut) {
		this.prevOut = prevOut;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}
}
