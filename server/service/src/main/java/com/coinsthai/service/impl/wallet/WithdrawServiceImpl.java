package com.coinsthai.service.impl.wallet;

import com.coinsthai.exception.BizErrorCode;
import com.coinsthai.exception.BizException;
import com.coinsthai.model.Wallet;
import com.coinsthai.model.Withdraw;
import com.coinsthai.module.audit.AuditAction;
import com.coinsthai.module.audit.AuditEntity;
import com.coinsthai.module.audit.AuditLogger;
import com.coinsthai.module.passcode.PasscodeService;
import com.coinsthai.pojo.intenum.WalletLogType;
import com.coinsthai.pojo.intenum.WithdrawStatus;
import com.coinsthai.repository.WithdrawRepository;
import com.coinsthai.service.CoinService;
import com.coinsthai.service.WalletService;
import com.coinsthai.service.WithdrawService;
import com.coinsthai.vo.BalanceUpdateRequest;
import com.coinsthai.vo.CoinView;
import com.coinsthai.vo.wallet.WithdrawFinishRequest;
import com.coinsthai.vo.wallet.WithdrawParameter;
import com.coinsthai.vo.wallet.WithdrawRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * @author
 */
@Service
public class WithdrawServiceImpl implements WithdrawService {

    @Autowired
    private WithdrawRepository withdrawRepository;

    @Autowired
    private WalletService walletService;

    @Autowired
    private CoinService coinService;

    @Autowired
    private PasscodeService passcodeService;

    @Autowired
    private AuditLogger auditLogger;

    @Transactional
    @Override
    public Withdraw apply(WithdrawRequest request) {
        if (StringUtils.isBlank(request.getAddress())) {
            BizException ex = new BizException(BizErrorCode.WITHDRAW_ADDRESS_EMPTY);
            auditLogger.fail(AuditAction.CREATING, AuditEntity.WITHDRAW, "", ex);
        }
        // TODO 临时取消验证码以测试
//        if (StringUtils.isAnyBlank(request.getPasscodeId(), request.getPasscode())) {
//            BizException ex = new BizException(BizErrorCode.WITHDRAW_PASSCODE_ILLEGAL);
//            auditLogger.fail(AuditAction.CREATING, AuditEntity.WITHDRAW, "", ex);
//        }
//        if (passcodeService.verify(request.getPasscodeId(), request.getPasscode(), true)) {
//            BizException ex = new BizException(BizErrorCode.WITHDRAW_PASSCODE_ILLEGAL);
//            auditLogger.fail(AuditAction.CREATING, AuditEntity.WITHDRAW, "", ex);
//        }

        Wallet wallet = walletService.get(request.getWalletId());
        if (wallet == null) {
            BizException ex = new BizException(BizErrorCode.WALLET_NOT_FOUND,
                                               "walletId",
                                               request.getWalletId());
            auditLogger.fail(AuditAction.CREATING, AuditEntity.WITHDRAW, "", ex);
        }

        if (!wallet.getUser().isBioVerify()) {
            BizException ex = new BizException(BizErrorCode.WITHDRAW_USER_UNVERIFIED,
                                               "userId",
                                               wallet.getUser().getId());
            auditLogger.fail(AuditAction.CREATING, AuditEntity.WITHDRAW, "", ex);
        }

        if (wallet.getAvailableBalance() < request.getVolume()) {
            BizException ex = new BizException(BizErrorCode.WALLET_BALANCE_EXCEED,
                                               "walletBalance,withdrawVolume",
                                               wallet.getAvailableBalance(),
                                               request.getVolume());
            auditLogger.fail(AuditAction.CREATING, AuditEntity.WITHDRAW, "", ex);
        }

        CoinView coin = coinService.get(wallet.getCoin().getId());
        if (request.getBrokerage() < coin.getMinNetworkBrokerage() ||
                request.getBrokerage() > coin.getMaxNetworkBrokerage()) {
            BizException ex = new BizException(BizErrorCode.WITHDRAW_BROKERAGE_ILLEGAL,
                                               "brokerage",
                                               request.getBrokerage());
            auditLogger.fail(AuditAction.CREATING, AuditEntity.WITHDRAW, "", ex);
        }

        Withdraw withdraw = new Withdraw();
        withdraw.setStatus(WithdrawStatus.PENDING);
        withdraw.setWallet(wallet);
        withdraw.setUser(wallet.getUser());
        withdraw.setCoin(wallet.getCoin());
        withdraw.setAddress(request.getAddress());
        withdraw.setVolume(request.getVolume());
        withdraw.setBrokerage(request.getBrokerage());
        Withdraw withdrawCreated = withdrawRepository.save(withdraw);
        freezeBalance(wallet.getId(), withdrawCreated);

        auditLogger.success(AuditAction.CREATED, AuditEntity.WITHDRAW, withdrawCreated.getId());

        return withdrawCreated;
    }

    @Transactional
    @Override
    public Withdraw revoke(Withdraw withdraw) {
        if (withdraw.getStatus() != WithdrawStatus.PENDING) {
            BizException ex = new BizException(BizErrorCode.WITHDRAW_STATUS_ILLEGAL, "id", withdraw.getId());
            auditLogger.fail("revoke", AuditEntity.WITHDRAW, withdraw.getId(), ex);
        }

        withdraw.setStatus(WithdrawStatus.REVOKED);
        Withdraw withdrawSaved = withdrawRepository.save(withdraw);
        walletService.unfreezeBalance(withdrawSaved);

        auditLogger.success("revoke", AuditEntity.WITHDRAW, withdraw.getId());
        return withdrawSaved;
    }

    @Transactional
    @Override
    public Withdraw process(String id) {
        Withdraw withdraw = get(id);
        if (withdraw.getStatus() != WithdrawStatus.PENDING) {
            BizException ex = new BizException(BizErrorCode.WITHDRAW_STATUS_ILLEGAL, "id", id);
            auditLogger.fail("process", AuditEntity.WITHDRAW, id, ex);
        }

        withdraw.setStatus(WithdrawStatus.REVOKED);
        Withdraw withdrawSaved = withdrawRepository.save(withdraw);

        auditLogger.success("process", AuditEntity.WITHDRAW, withdraw.getId());
        return withdrawSaved;
    }

    @Transactional
    @Override
    public Withdraw decline(String id) {
        Withdraw withdraw = get(id);
        if (withdraw.getStatus() != WithdrawStatus.PENDING && withdraw.getStatus() != WithdrawStatus.PROCESSING) {
            BizException ex = new BizException(BizErrorCode.WITHDRAW_STATUS_ILLEGAL,
                                               "id,status",
                                               id,
                                               withdraw.getStatus());
            auditLogger.fail("decline", AuditEntity.WITHDRAW, id, ex);
        }

        withdraw.setStatus(WithdrawStatus.DECLINED);
        Withdraw withdrawSaved = withdrawRepository.save(withdraw);
        walletService.unfreezeBalance(withdrawSaved);

        auditLogger.success("decline", AuditEntity.WITHDRAW, withdraw.getId());
        return withdrawSaved;
    }

    @Transactional
    @Override
    public Withdraw approve(String id) {
        Withdraw withdraw = get(id);
        if (withdraw.getStatus() != WithdrawStatus.PENDING && withdraw.getStatus() != WithdrawStatus.PROCESSING) {
            BizException ex = new BizException(BizErrorCode.WITHDRAW_STATUS_ILLEGAL, "id", id);
            auditLogger.fail("approve", AuditEntity.WITHDRAW, id, ex);
        }

        withdraw.setStatus(WithdrawStatus.APPROVED);
        Withdraw withdrawSaved = withdrawRepository.save(withdraw);

        auditLogger.success("approve", AuditEntity.WITHDRAW, withdraw.getId());
        return withdrawSaved;
    }

    @Transactional
    @Override
    public Withdraw finish(WithdrawFinishRequest request) {
        Withdraw withdraw = get(request.getId());
        if (withdraw.getStatus() != WithdrawStatus.PENDING && withdraw.getStatus() != WithdrawStatus.PROCESSING &&
                withdraw.getStatus() != WithdrawStatus.APPROVED) {
            BizException ex = new BizException(BizErrorCode.WITHDRAW_STATUS_ILLEGAL, "id", request.getId());
            auditLogger.fail("finish", AuditEntity.WITHDRAW, request.getId(), ex);
        }

        withdraw.setStatus(WithdrawStatus.FINISHED);
        withdraw.setTxid(request.getTxid());
        Withdraw withdrawSaved = withdrawRepository.save(withdraw);
        walletService.updateBalanceForWithdraw(withdrawSaved);

        auditLogger.success("finish", AuditEntity.WITHDRAW, withdraw.getId());
        return withdrawSaved;
    }

    @Override
    public Withdraw get(String id) {
        Withdraw withdraw = withdrawRepository.findOne(id);
        if (withdraw == null) {
            BizException ex = new BizException(BizErrorCode.WITHDRAW_NOT_FOUND, "id", id);
            auditLogger.fail(AuditAction.VIEWING, AuditEntity.WITHDRAW, id, ex);
        }

        return withdraw;
    }

    @Override
    public Page<Withdraw> page(WithdrawParameter parameter) {
        return withdrawRepository.findByPage(parameter);
    }

    private Wallet freezeBalance(String walletId, Withdraw withdraw) {
        BalanceUpdateRequest balanceUpdateRequest = new BalanceUpdateRequest();
        balanceUpdateRequest.setId(walletId);
        balanceUpdateRequest.setVolume(withdraw.getVolume());
        balanceUpdateRequest.setType(WalletLogType.FREEZE);
        balanceUpdateRequest.setBizId(withdraw.getId());
        return walletService.freezeBalance(balanceUpdateRequest);
    }

}
