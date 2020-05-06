package com.dmg.gameconfigserver.service.financing.impl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dmg.common.core.web.BusinessException;
import com.dmg.data.client.NettySend;
import com.dmg.data.common.dto.GoldPaySendDto;
import com.dmg.gameconfigserver.common.enums.ResultEnum;
import com.dmg.gameconfigserver.dao.finance.PlatformRechargeLogDao;
import com.dmg.gameconfigserver.model.bean.finance.PlatformRechargeLogBean;
import com.dmg.gameconfigserver.model.dto.finance.PlatformRechargeDTO;
import com.dmg.gameconfigserver.model.dto.finance.QueryAbnormalOrderDTO;
import com.dmg.gameconfigserver.service.financing.PlatformRechargeLogService;
import com.dmg.server.common.enums.AccountChangeTypeEnum;
import com.dmg.server.common.enums.PlatformRechargeStatusEnum;

/**
 * @Description
 * @Author mice
 * @Date 2019/12/27 10:28
 * @Version V1.0
 **/
@Service
public class PlatformRechargeLogServiceImpl implements PlatformRechargeLogService {

    @Autowired
    private PlatformRechargeLogDao platformRechargeLogDao;

    @Autowired
    private NettySend nettySend;

    @Override
    public IPage<PlatformRechargeLogBean> queryPlatfromRechargePage(PlatformRechargeDTO platformRechargeDTO) {
        IPage<PlatformRechargeLogBean> platformRechargeLogBeanPage = this.platformRechargeLogDao.selectPage(platformRechargeDTO.getPageCondition(), new LambdaQueryWrapper<PlatformRechargeLogBean>()
                .eq(platformRechargeDTO.getUserId() != null, PlatformRechargeLogBean::getUserId, platformRechargeDTO.getUserId())
                .eq(StringUtils.isNotEmpty(platformRechargeDTO.getOrderId()), PlatformRechargeLogBean::getOrderId, platformRechargeDTO.getOrderId())
                .eq(StringUtils.isNotEmpty(platformRechargeDTO.getThirdOrderId()), PlatformRechargeLogBean::getThirdOrderId, platformRechargeDTO.getThirdOrderId())
                .eq(platformRechargeDTO.getOrderStatus() != null, PlatformRechargeLogBean::getOrderStatus, platformRechargeDTO.getOrderStatus())
                .eq(platformRechargeDTO.getPlatformId() != null, PlatformRechargeLogBean::getPlatformId, platformRechargeDTO.getPlatformId())
                .gt(platformRechargeDTO.getStartDate() != null, PlatformRechargeLogBean::getCreateDate, platformRechargeDTO.getStartDate())
                .lt(platformRechargeDTO.getEndDate() != null, PlatformRechargeLogBean::getCreateDate, platformRechargeDTO.getEndDate()));
        return platformRechargeLogBeanPage;
    }

    @Override
    public IPage<PlatformRechargeLogBean> queryAbnormalOrder(QueryAbnormalOrderDTO queryAbnormalOrderDTO) {
        IPage<PlatformRechargeLogBean> platformRechargeLogBeanPage = this.platformRechargeLogDao.selectPage(queryAbnormalOrderDTO.getPageCondition(), new LambdaQueryWrapper<PlatformRechargeLogBean>()
                .eq(StringUtils.isNotEmpty(queryAbnormalOrderDTO.getOrderId()), PlatformRechargeLogBean::getOrderId, queryAbnormalOrderDTO.getOrderId())
                .in(PlatformRechargeLogBean::getOrderStatus, Arrays.asList(PlatformRechargeStatusEnum.NO_ARRIVE.getCode(), PlatformRechargeStatusEnum.PAY_FAIL.getCode())));

        return platformRechargeLogBeanPage;
    }

    @Override
    public String replenishOrder(Long id, Long dealUserId) {
        PlatformRechargeLogBean platformRechargeLogBean = this.platformRechargeLogDao.selectById(id);
        if (platformRechargeLogBean == null) {
            throw new BusinessException(ResultEnum.PARAM_ERROR.getCodeStr(), ResultEnum.PARAM_ERROR.getMsg());
        }
        if (platformRechargeLogBean.getOrderStatus() != PlatformRechargeStatusEnum.PAY_FAIL.getCode() &&
                platformRechargeLogBean.getOrderStatus() != PlatformRechargeStatusEnum.NO_ARRIVE.getCode()) {
            throw new BusinessException(ResultEnum.ORDER_STATUS_ERROR.getCodeStr(), ResultEnum.ORDER_STATUS_ERROR.getMsg());
        }
        GoldPaySendDto goldPaySendDto = new GoldPaySendDto(platformRechargeLogBean.getUserId(), platformRechargeLogBean.getRechargeAmount(), AccountChangeTypeEnum.CODE_CHANNEL_RECHARGE.getCode());
        this.nettySend.goldPayAsync(goldPaySendDto);

        platformRechargeLogBean.setOrderStatus(PlatformRechargeStatusEnum.REPLENISH_SUCCESS.getCode());
        platformRechargeLogBean.setDealUserId(dealUserId);
        platformRechargeLogBean.setDealDate(new Date());
        platformRechargeLogBean.setArriveDate(new Date());
        this.platformRechargeLogDao.updateById(platformRechargeLogBean);
        return platformRechargeLogBean.getOrderId();
    }

    @Override
    public Long getFirstRechargeCount() {
        List<Long> ids = this.platformRechargeLogDao.countTodayRecharge();
        if (ids == null || ids.size() < 1) {
            return Long.valueOf(0L);
        }
        return ids.size() - this.platformRechargeLogDao.countRecharge(ids);
    }

    @Override
    public BigDecimal getSumRechargeAmount() {
        BigDecimal data = this.platformRechargeLogDao.countTodayRechargeAmount();
        return data == null ? BigDecimal.ZERO : data;
    }

    @Override
    public BigDecimal getSumFirstRechargeAmount() {
        BigDecimal data = this.platformRechargeLogDao.countTodayFirstRechargeAmount();
        return data == null ? BigDecimal.ZERO : data;
    }
}