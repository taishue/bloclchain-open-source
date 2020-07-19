package com.coinsthai.startup;

import com.coinsthai.cache.CoinCache;
import com.coinsthai.model.Coin;
import com.coinsthai.model.Market;
import com.coinsthai.pojo.blockchain.CoinType;
import com.coinsthai.pojo.intenum.CoinCategory;
import com.coinsthai.repository.CoinRepository;
import com.coinsthai.repository.MarketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by
 */
@Component
public class C01_MarketInitializer implements StartupInitializer {

    @Autowired
    private CoinRepository coinRepository;

    @Autowired
    private MarketRepository marketRepository;

    @Autowired
    private CoinCache coinCache;

    @Override
    public boolean accept() {
        // 如果缓存中已有记录，则不执行
        return coinCache.listAll().isEmpty();
    }

    @Override
    public void initialize() {
        Coin eqt = createCoin(CoinType.EQT.name(), "EqualThai", CoinCategory.LEGAL, 100, true, false, 1);
        Coin usdt = createCoin(CoinType.USDT.name(), "TetherUS", CoinCategory.LEGAL, 100, true, false, 4);

        Coin btc = createCoin(CoinType.BTC.name(), "BitCoin", CoinCategory.DIGITAL, 100000000, true, true, 10);
        Coin eth = createCoin(CoinType.ETH.name(), "Ethereum", CoinCategory.DIGITAL, 100000000, false, true, 11);//暂时将ETH设置为非基准
        Coin etc = createCoin(CoinType.ETC.name(), "Ethereum Classic", CoinCategory.DIGITAL, 100000000, false, true, 12);
        Coin xrp = createCoin(CoinType.XRP.name(), "Ripple", CoinCategory.DIGITAL, 100000000, false, true, 13);
        Coin bch = createCoin(CoinType.BCH.name(), "Bitcoin Cash", CoinCategory.DIGITAL, 100000000, false, true, 14);

        Coin ltc = createCoin(CoinType.LTC.name(), "Litecoin", CoinCategory.DIGITAL, 100000000, false, true, 16);
        Coin xlm = createCoin(CoinType.XLM.name(), "Stellar", CoinCategory.DIGITAL, 100000000, false, true, 17);

        // ETH代币
        Coin ven = createCoin(CoinType.VEN.name(), "VeChain", CoinCategory.DIGITAL, 100000000, false, false, 21,
                              eth, "0xd850942ef8811f2a866692a623011bde52a462c1");
        Coin omg = createCoin(CoinType.OMG.name(), "OmiseGO", CoinCategory.DIGITAL, 100000000, false, false, 23,
                              eth, "0xd26114cd6EE289AccF82350c8d8487fedB8A0C07");
        Coin zil = createCoin(CoinType.ZIL.name(), "Zilliqa", CoinCategory.DIGITAL, 100000000, false, false, 25,
                              eth, "0x05f4a42e251f2d52b8ed15e9fedaacfcef1fad27");
        Coin zrx = createCoin(CoinType.ZRX.name(), "ZRX", CoinCategory.DIGITAL, 100000000, false, false, 26,
                              eth, "0xe41d2489571d322189246dafa5ebde1f4699f498");
        Coin rep = createCoin(CoinType.REP.name(), "REP", CoinCategory.DIGITAL, 100000000, false, false, 27,
                              eth, "0xe94327d07fc17907b4db788e5adf2ed424addff6");
        Coin ivp = createCoin(CoinType.IVP.name(), "iLoveParty", CoinCategory.DIGITAL, 100000000, false, false, 28,
                              eth, "0x5a1781e12c832b7a7747bacb8448c6ae58eab363");


        Coin dcr = createCoin(CoinType.DCR.name(), "Decred", CoinCategory.DIGITAL, 100000000, false, false, 39);

        // ===== 市场 ======
        // EQT
        createMarket(eqt, btc, false);
        createMarket(eqt, eth, false);
        createMarket(eqt, ven, false);
        createMarket(eqt, omg, false);
        createMarket(eqt, xrp, false);
        createMarket(eqt, dcr, false);

        // USDT
        createMarket(usdt, btc, false);
        createMarket(usdt, eth, false);
        createMarket(usdt, xrp, false);
        createMarket(usdt, dcr, false);

        // BTC
        createMarket(btc, bch, true);
        createMarket(btc, eth, true);
        createMarket(btc, etc, true);
        createMarket(btc, xrp, true);
        createMarket(btc, ltc, true);
        createMarket(btc, xlm, true);

        createMarket(btc, ven, false);
        createMarket(btc, omg, false);
        createMarket(btc, zil, false);
        createMarket(btc, zrx, false);
        createMarket(btc, rep, false);

        // ETH
        createMarket(eth, ven, false);
        createMarket(eth, omg, false);
        createMarket(eth, xrp, false);
        createMarket(eth, zil, false);
        createMarket(eth, zrx, false);
        createMarket(eth, rep, false);
        createMarket(eth, ivp, false);
    }

    private Market createMarket(Coin base, Coin target, boolean active) {
        String name = target.getName() + "/" + base.getName();
        Market market = marketRepository.findByTargetIdAndBaseId(target.getId(), base.getId());
        if (market == null) {
            market = new Market();
        }

        market.setName(name);
        market.setBase(base);
        market.setTarget(target);
        market.setActive(active);
        return marketRepository.save(market);
    }

    private Coin createCoin(String name, String fullName, CoinCategory category, int unit, boolean base,
                            boolean active, int priority) {
        return createCoin(name, fullName, category, unit, base, active, priority, null, null);
    }


    private Coin createCoin(String name, String fullName, CoinCategory category, int unit, boolean base,
                            boolean active, int priority, Coin tokenOn, String contract) {
        Coin coin = coinRepository.findByName(name);
        if (coin == null) {
            coin = new Coin();
        }

        coin.setName(name);
        coin.setFullName(fullName);
        coin.setUnit(unit);
        coin.setBase(base);
        coin.setCategory(category);
        coin.setActive(active);
        coin.setPriority(priority);
        coin.setTokenOn(tokenOn);
        coin.setContract(contract);
        return coinRepository.save(coin);
    }
}
