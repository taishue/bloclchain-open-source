package com.coinsthai.repository;

import com.coinsthai.model.WalletLog;
import com.coinsthai.pojo.parametric.WalletLogParametric;
import org.springframework.data.domain.Page;

/**
 * @author 
 */
public interface WalletLogCustomRepository {

    Page<WalletLog> findByPage(WalletLogParametric parametric);

}
