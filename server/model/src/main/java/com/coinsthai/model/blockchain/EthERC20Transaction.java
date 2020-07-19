package com.coinsthai.model.blockchain;

import com.coinsthai.model.ModelUtils;
import com.coinsthai.pojo.blockchain.EthERC20TransactionPojo;

import javax.persistence.*;
import java.util.Date;

/**
 * @author YeYifeng
 */
@Entity
@Table(name = "t_eth_erc20_transaction")
public class EthERC20Transaction extends EthERC20TransactionPojo {

	@Id
	@Access(AccessType.PROPERTY)
	@Override
	public String getId() {
		return super.getId();
	}

	@Column
	@Override
	public long getBlockNumber() {
		return super.getBlockNumber();
	}

	@Column
	@Override
	public long getTimeStamp() {
		return super.getTimeStamp();
	}

	@Column
	@Override
	public String getTxHash() {
		return super.getTxHash();
	}

	@Column
	@Override
	public int getNonce() {
		return super.getNonce();
	}

	@Column
	@Override
	public String getBlockHash() {
		return super.getBlockHash();
	}

	@Column
	@Override
	public String getFromAddress() {
		return super.getFromAddress();
	}

	@Column
	@Override
	public String getContractAddress() {
		return super.getContractAddress();
	}

	@Column
	@Override
	public String getToAddress() {
		return super.getToAddress();
	}

	@Column
	@Override
	public String getTxValue() {
		return super.getTxValue();
	}

	@Column
	@Override
	public String getTokenName() {
		return super.getTokenName();
	}

	@Column
	@Override
	public String getTokenSymbol() {
		return super.getTokenSymbol();
	}

	@Column
	@Override
	public int getTokenDecimal() {
		return super.getTokenDecimal();
	}

	@Column
	@Override
	public int getTransactionIndex() {
		return super.getTransactionIndex();
	}

	@Column
	@Override
	public String getGas() {
		return super.getGas();
	}

	@Column
	@Override
	public String getGasPrice() {
		return super.getGasPrice();
	}

	@Column
	@Override
	public String getGasUsed() {
		return super.getGasUsed();
	}

	@Column
	@Override
	public String getCumulativeGasUsed() {
		return super.getCumulativeGasUsed();
	}

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column
	@Override
	public String getInput() {
		return super.getInput();
	}

	@Column
	@Override
	public int getConfirmations() {
		return super.getConfirmations();
	}

	@Column
	@Override
	public Date getCreatedAt() {
		return super.getCreatedAt();
	}

	@Column
	@Override
	public Date getModifiedAt() {
		return super.getModifiedAt();
	}

	@PrePersist
	protected void prePersist() {
		ModelUtils.prePersist(this);
	}

	@PreUpdate
	protected void preUpdate() {
		ModelUtils.preUpdate(this);
	}
}
