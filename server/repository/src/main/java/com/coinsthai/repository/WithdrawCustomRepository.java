package com.coinsthai.repository;

import com.coinsthai.model.Withdraw;
import com.coinsthai.pojo.parametric.WithdrawParametric;
import org.springframework.data.domain.Page;

/**
 * @author 
 */
public interface WithdrawCustomRepository {

    Page<Withdraw> findByPage(WithdrawParametric parametric);

}
