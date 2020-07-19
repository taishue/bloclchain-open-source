package com.coinsthai.service;

import com.coinsthai.model.Withdraw;
import com.coinsthai.vo.wallet.WithdrawFinishRequest;
import com.coinsthai.vo.wallet.WithdrawParameter;
import com.coinsthai.vo.wallet.WithdrawRequest;
import org.springframework.data.domain.Page;

/**
 * @author
 */
public interface WithdrawService {

    /**
     * 申请提现
     *
     * @param request
     * @return
     */
    Withdraw apply(WithdrawRequest request);

    /**
     * 撤销提现申请
     *
     * @param withdraw
     * @return
     */
    Withdraw revoke(Withdraw withdraw);

    /**
     * 将提现申请状态改为处理中
     * @param id
     * @return
     */
    Withdraw process(String id);

    /**
     * 拒绝提现申请
     *
     * @param id
     * @return
     */
    Withdraw decline(String id);

    /**
     * 通过提现审核
     *
     * @param id
     * @return
     */
    Withdraw approve(String id);

    /**
     * 完成提现，回填信息
     *
     * @param request
     * @return
     */
    Withdraw finish(WithdrawFinishRequest request);

    Withdraw get(String id);

    Page<Withdraw> page(WithdrawParameter parameter);
}
