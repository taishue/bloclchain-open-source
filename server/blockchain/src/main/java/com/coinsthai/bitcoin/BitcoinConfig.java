package com.coinsthai.bitcoin;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author 
 */
@Component
public class BitcoinConfig {

    @Value("${app.bitcoin.wallet.path}")
    private String walletPath;

    @Value("${app.bitcoin.wallet.password}")
    private String walletPassword;

    @Value("${app.bitcoin.wallet.random.salt}")
    private String randomSalt;  //TODO random salt是否应该每次随机生成

    public String getWalletPath() {
        return walletPath;
    }

    public void setWalletPath(String walletPath) {
        this.walletPath = walletPath;
    }

    public String getWalletPassword() {
        return walletPassword;
    }

    public void setWalletPassword(String walletPassword) {
        this.walletPassword = walletPassword;
    }

    public String getRandomSalt() {
        return randomSalt;
    }

    public void setRandomSalt(String randomSalt) {
        this.randomSalt = randomSalt;
    }
}
