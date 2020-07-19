package com.coinsthai.service.impl;

import com.coinsthai.module.audit.AuditAction;
import com.coinsthai.module.audit.AuditEntity;
import com.coinsthai.module.audit.AuditLogger;
import com.coinsthai.cache.CoinCache;
import com.coinsthai.cache.NameCache;
import com.coinsthai.converter.ModelFromViewConverter;
import com.coinsthai.converter.SimpleConverter;
import com.coinsthai.exception.BizErrorCode;
import com.coinsthai.exception.BizException;
import com.coinsthai.model.Coin;
import com.coinsthai.repository.CoinRepository;
import com.coinsthai.service.CoinService;
import com.coinsthai.vo.CoinView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author
 */
@Service
public class CoinServiceImpl implements CoinService {

    @Autowired
    private AuditLogger auditLogger;

    @Autowired
    private CoinRepository coinRepository;

    @Autowired
    private CoinCache coinCache;

    @Autowired
    private NameCache nameCache;

    @Autowired
    private ModelFromViewConverter<Coin, CoinView> modelConverter;

    @Autowired
    private SimpleConverter<CoinView, Coin> viewConverter;

    @Transactional
    @Override
    public CoinView create(CoinView view) {
        if (StringUtils.isBlank(view.getName())) {
            BizException ex = new BizException(BizErrorCode.COIN_NAME_EMPTY);
            auditLogger.fail(AuditAction.CREATING, AuditEntity.COIN, "", ex);
        }
        if (StringUtils.isBlank(view.getFullName())) {
            BizException ex = new BizException(BizErrorCode.COIN_FULL_NAME_EMPTY);
            auditLogger.fail(AuditAction.CREATING, AuditEntity.COIN, "", ex);
        }

        if (coinRepository.findByName(view.getName()) != null) {
            BizException ex = new BizException(BizErrorCode.COIN_NAME_EXISTS, "name", view.getName());
            auditLogger.fail(AuditAction.CREATING, AuditEntity.COIN, "", ex);
        }
        if (coinRepository.findByFullName(view.getFullName()) != null) {
            BizException ex = new BizException(BizErrorCode.COIN_FULL_NAME_EXISTS, "fullName", view.getFullName());
            auditLogger.fail(AuditAction.CREATING, AuditEntity.COIN, "", ex);
        }
        if (StringUtils.isBlank(view.getTokenOnId()) && StringUtils.isNotBlank(view.getContract())) {
            BizException ex = new BizException(BizErrorCode.COIN_TOKEN_ILLEGAL);
            auditLogger.fail(AuditAction.CREATING, AuditEntity.COIN, "", ex);
        }
        if (StringUtils.isNotBlank(view.getTokenOnId()) && StringUtils.isBlank(view.getContract())) {
            BizException ex = new BizException(BizErrorCode.COIN_TOKEN_ILLEGAL);
            auditLogger.fail(AuditAction.CREATING, AuditEntity.COIN, "", ex);
        }


        Coin model = new Coin();
        modelConverter.toPojo(view, model);

        if (StringUtils.isNotBlank(view.getTokenOnId())) {
            Coin tokenOn = coinRepository.findOne(view.getTokenOnId());
            if (tokenOn == null) {
                BizException ex = new BizException(BizErrorCode.COIN_NOT_FOUND, "id", view.getTokenOnId());
                auditLogger.fail(AuditAction.CREATING, AuditEntity.COIN, "", ex);
            }
            model.setTokenOn(tokenOn);
        }

        coinRepository.save(model);
        auditLogger.success(AuditAction.CREATED, AuditEntity.COIN, model.getId());

        CoinView createdView = new CoinView();
        viewConverter.toPojo(model, createdView);
        coinCache.set(createdView.getId(), createdView);

        return createdView;
    }

    @Override
    public CoinView get(String id) {
        CoinView view = coinCache.get(id);
        if (view == null) {
            Coin model = coinRepository.findOne(id);
            if (model == null) {
                BizException ex = new BizException(BizErrorCode.COIN_NOT_FOUND, "id", id);
                auditLogger.fail(AuditAction.VIEWING, AuditEntity.COIN, id, ex);
            }

            view = new CoinView();
            viewConverter.toPojo(model, view);
            coinCache.set(id, view);
        }

        return view;
    }

    @Override
    public CoinView getByName(String name) {
        String id = nameCache.get(name);
        if (StringUtils.isNotBlank(id)) {
            return get(id);
        }

        Coin coin = coinRepository.findByName(name);
        CoinView view = new CoinView();
        viewConverter.toPojo(coin, view);
        return view;
    }

    @Override
    public List<CoinView> listAll() {
        List<CoinView> views = coinCache.listAll();
        if (views.isEmpty()) {
            List<Coin> models = coinRepository.findAllByOrderByPriorityAsc();
            models.forEach(model -> {
                CoinView view = new CoinView();
                viewConverter.toPojo(model, view);
                views.add(view);
            });
            coinCache.setAll(views);
        }

        return views;
    }

    @Override
    public List<CoinView> listActives() {
        List<CoinView> all = listAll();
        return all.stream().filter(view -> view.isActive()).collect(Collectors.toList());
    }
}
