package com.coinsthai.service.impl;

import com.coinsthai.exception.BizErrorCode;
import com.coinsthai.exception.BizException;
import com.coinsthai.model.Wallet;
import com.coinsthai.model.blockchain.PhysicalWallet;
import com.coinsthai.pojo.blockchain.CoinType;
import com.coinsthai.repository.PhysicalWalletRepository;
import com.coinsthai.service.CoinService;
import com.coinsthai.service.PhysicalWalletService;
import com.coinsthai.vo.CoinView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 
 */
@Service
public class PhysicalWalletServiceImpl implements PhysicalWalletService {

    @Autowired
    private PhysicalWalletRepository repository;

    @Autowired
    private CoinService coinService;

    @Override
    public PhysicalWallet create(Wallet wallet) {
        PhysicalWallet physicalWallet = repository.findByWalletId(wallet.getId());
        if (physicalWallet != null) {
            return physicalWallet;
        }

        physicalWallet = new PhysicalWallet();
        physicalWallet.setWallet(wallet);
        physicalWallet.setAddress(wallet.getAddress());
        physicalWallet.setBalance(0l);

        CoinView coin = coinService.get(wallet.getCoin().getId());
        CoinType coinType = CoinType.valueOf(coin.getName());
        physicalWallet.setCoinType(coinType);

        return repository.save(physicalWallet);
    }

    @Override
    public PhysicalWallet updateBalance(String walletId, long balance) {
        PhysicalWallet wallet = repository.findByWalletId(walletId);
        if (wallet == null) {
            throw new BizException(BizErrorCode.WALLET_NOT_FOUND);
        }

        wallet.setBalance(balance);
        return repository.save(wallet);
    }

    @Override
    public PhysicalWallet updateBalance(String address, CoinType coinType, long balance) {
        PhysicalWallet wallet = repository.findByAddressAndCoinType(address, coinType);
        if (wallet == null) {
            throw new BizException(BizErrorCode.WALLET_NOT_FOUND);
        }

        wallet.setBalance(balance);
        return repository.save(wallet);
    }

    @Override
    public PhysicalWallet increaseBalance(String address, CoinType coinType, long volume) {
        PhysicalWallet wallet = repository.findByAddressAndCoinType(address, coinType);
        if (wallet == null) {
            throw new BizException(BizErrorCode.WALLET_NOT_FOUND);
        }

        wallet.setBalance(wallet.getBalance() + volume);
        return repository.save(wallet);
    }

    @Override
    public PhysicalWallet getByWallet(String walletId) {
        return repository.findByWalletId(walletId);
    }

    @Override
    public PhysicalWallet getByAddressAndType(String address, CoinType type) {
        return repository.findByAddressAndCoinType(address, type);
    }
}
