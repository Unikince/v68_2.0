package com.dmg.gameconfigserver.service.financing.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dmg.common.core.web.BusinessException;
import com.dmg.gameconfigserver.common.enums.ResultEnum;
import com.dmg.gameconfigserver.dao.finance.FinanceBankCardDao;
import com.dmg.gameconfigserver.model.bean.finance.FinanceBankCardBean;
import com.dmg.gameconfigserver.model.vo.finance.FinanceBankCardListRecv;
import com.dmg.gameconfigserver.service.financing.FinanceBankCardService;

/**
 * 财务银行卡
 */
@Service
public class FinanceBankCardServiceImpl implements FinanceBankCardService {
    @Autowired
    private FinanceBankCardDao dao;

    @Override
    public synchronized void saveOne(FinanceBankCardBean bean) {
        if (bean == null) {
            throw new BusinessException("" + ResultEnum.PARAM_ERROR.getCode(), "参数为空");
        }
        int count = this.dao.selectCount(null);
        bean.setId(count + 1L);
        if (bean.getMoney() == null) {
            bean.setMoney(BigDecimal.ZERO);
        }
        this.dao.insert(bean);

    }

    @Override
    public synchronized void delete(Long id) {
        if (id == null) {
            throw new BusinessException("" + ResultEnum.PARAM_ERROR.getCode(), "参数为空");
        }
        this.dao.deleteById(id);

        LambdaQueryWrapper<FinanceBankCardBean> listWrapper = new LambdaQueryWrapper<>();
        listWrapper.orderByAsc(FinanceBankCardBean::getId);
        List<FinanceBankCardBean> beans = this.dao.selectList(listWrapper);

        for (int i = 0; i < beans.size(); i++) {
            FinanceBankCardBean bean = beans.get(i);
            this.dao.sortId(bean.getId(), i + 1L);
        }
    }

    @Override
    public void updateOne(FinanceBankCardBean bean) {
        if (bean == null) {
            throw new BusinessException("" + ResultEnum.PARAM_ERROR.getCode(), "参数为空");
        }
        this.dao.updateById(bean);
    }

    @Override
    public FinanceBankCardBean get(Long id) {
        if (id == null) {
            throw new BusinessException("" + ResultEnum.PARAM_ERROR.getCode(), "参数为空");
        }
        return this.dao.selectById(id);
    }

    @Override
    public IPage<FinanceBankCardBean> getList(FinanceBankCardListRecv reqVo) {
        IPage<FinanceBankCardBean> page = new Page<>(reqVo.getCurrent(), reqVo.getSize());
        return this.dao.selectPage(page, null);
    }

    @Override
    public void payMoney(Long id, BigDecimal money) {
        FinanceBankCardBean bean = this.get(id);
        bean.setMoney(bean.getMoney().add(money));
        this.dao.updateById(bean);
    }

}
