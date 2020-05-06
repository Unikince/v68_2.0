package com.dmg.gameconfigserver.service.financing.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dmg.common.core.web.BusinessException;
import com.dmg.gameconfigserver.common.enums.ResultEnum;
import com.dmg.gameconfigserver.dao.finance.PayChannelDao;
import com.dmg.gameconfigserver.model.bean.finance.PayChannelBean;
import com.dmg.gameconfigserver.model.vo.finance.PayChannelListRecv;
import com.dmg.gameconfigserver.service.financing.PayChannelService;

/**
 * 支付渠道
 */
@Service
public class PayChannelServiceImpl implements PayChannelService {
    @Autowired
    private PayChannelDao dao;

    @Override
    public synchronized void saveOne(PayChannelBean bean) {
        if (bean == null) {
            throw new BusinessException("" + ResultEnum.PARAM_ERROR.getCode(), "参数为空");
        }
        this.dao.insert(bean);
    }

    @Override
    public synchronized void delete(Long id) {
        if (id == null) {
            throw new BusinessException("" + ResultEnum.PARAM_ERROR.getCode(), "参数为空");
        }
        this.dao.deleteById(id);
    }

    @Override
    public void updateOne(PayChannelBean bean) {
        if (bean == null) {
            throw new BusinessException("" + ResultEnum.PARAM_ERROR.getCode(), "参数为空");
        }
        this.dao.updateById(bean);
    }

    @Override
    public PayChannelBean get(Long id) {
        if (id == null) {
            throw new BusinessException("" + ResultEnum.PARAM_ERROR.getCode(), "参数为空");
        }
        return this.dao.selectById(id);
    }

    @Override
    public IPage<PayChannelBean> getList(PayChannelListRecv reqVo) {
        IPage<PayChannelBean> page = new Page<>(reqVo.getCurrent(), reqVo.getSize());
        LambdaQueryWrapper<PayChannelBean> listWrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.equals(ALL_CHANNEL, reqVo.getChannel())) {
            listWrapper.eq(PayChannelBean::getChannel, reqVo.getChannel());
        }
        if (reqVo.getStatus() != null) {
            listWrapper.eq(PayChannelBean::isStatus, reqVo.getStatus());
        }
        listWrapper.orderByAsc(PayChannelBean::getSort);
        return this.dao.selectPage(page, listWrapper);
    }

    @Override
    public List<String> channelGroup() {
        List<PayChannelBean> beans = this.dao.selectList(null);
        Set<String> result = new HashSet<>();
        result.add(ALL_CHANNEL);
        if (beans != null) {
            for (PayChannelBean bean : beans) {
                result.add(bean.getChannel());
            }
        }
        return new ArrayList<>(result);
    }
}
