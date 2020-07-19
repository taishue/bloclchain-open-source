package com.coinsthai.repository;

import com.coinsthai.model.Wallet;
import javax.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * @author YeYifeng
 */
public interface WalletRepository extends AbstractRepository<Wallet> {


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "from Wallet where id=:id ")
    Wallet findOneForUpdate(@Param("id") String id);

    Wallet findByUserIdAndCoinId(String userId, String coinId);

    Wallet findByAddressAndCoinId(String address, String coinId);

    long countByUserIdAndCoinId(String userId, String coinId);

    long countByAddressAndUserIdNot(String address, String userId);

    List<Wallet> findByUserId(String userId);

    @Query("select wallet from Wallet as wallet inner join fetch wallet.coin as coin where coin.name=:coinName")
    List<Wallet> findByCoinName(@Param("coinName") String coinName);

    @Query("select wallet from Wallet as wallet " +
           "inner join fetch wallet.coin as coin " +
           "inner join coin.tokenOn as parentCoin " +
           "where wallet.address=:address and parentCoin.name=:parentCoinName")
    List<Wallet> findByCoinTokenOnName(@Param("address") String address,
                                       @Param("parentCoinName") String parentCoinName);
}
