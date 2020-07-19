package com.coinsthai.model.blockchain;

import com.coinsthai.model.ModelUtils;
import com.coinsthai.model.Wallet;
import com.coinsthai.pojo.blockchain.BtcTransactionPojo;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * @author YeYifeng
 */
@Entity
@Table(name = "t_btc_transaction")
public class BtcTransaction extends BtcTransactionPojo {

	private Wallet wallet;

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Access(AccessType.PROPERTY)
	@Override
	public String getId() {
		return super.getId();
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "wallet_id")
	public Wallet getWallet() {
		return wallet;
	}

	public void setWallet(Wallet wallet) {
		this.wallet = wallet;
	}

	@Column
	@Override
	public int getVer() {
		return super.getVer();
	}

	@Column
	@Override
	public int getWeight() {
		return super.getWeight();
	}

	@Column
	@Override
	public int getBlockHeight() {
		return super.getBlockHeight();
	}

	@Column
	@Override
	public String getRelayedBy() {
		return super.getRelayedBy();
	}

	@Column
	@Override
	public long getLockTime() {
		return super.getLockTime();
	}

	@Column
	@Override
	public String getResult() {
		return super.getResult();
	}

	@Column
	@Override
	public int getSize() {
		return super.getSize();
	}

	@Column
	@Override
	public long getTxTime() {
		return super.getTxTime();
	}

	@Column
	@Override
	public long getTxIndex() {
		return super.getTxIndex();
	}

	@Column
	@Override
	public int getVinSz() {
		return super.getVinSz();
	}

	@Column
	@Override
	public String getTxHash() {
		return super.getTxHash();
	}

	@Column
	@Override
	public int getVoutSz() {
		return super.getVoutSz();
	}

	@Column
	@Override
	public String getFromAddress() {
		return super.getFromAddress();
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
	public boolean isConfirmed() {
		return super.isConfirmed();
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
