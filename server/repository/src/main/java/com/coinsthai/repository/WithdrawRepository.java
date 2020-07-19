package com.coinsthai.repository;

import com.coinsthai.model.Withdraw;
import com.coinsthai.pojo.intenum.WithdrawStatus;

import java.util.Date;
import java.util.List;

/**
 * @author
 */
public interface WithdrawRepository extends AbstractRepository<Withdraw>, WithdrawCustomRepository {

    List<Withdraw> findByStatusAndCreatedAtLessThan(WithdrawStatus status, Date date);
}
