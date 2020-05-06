package com.dmg.gameconfigserver.service.financing;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dmg.gameconfigserver.model.bean.finance.PlatformRechargeLogBean;
import com.dmg.gameconfigserver.model.dto.finance.PlatformRechargeDTO;
import com.dmg.gameconfigserver.model.dto.finance.QueryAbnormalOrderDTO;

import java.math.BigDecimal;

/**
 * @Description
 * @Author mice
 * @Date 2019/12/27 10:13
 * @Version V1.0
 **/
public interface PlatformRechargeLogService {


    /**
     * @description: 分页查询平台充值记录
     * @param platformRechargeDTO
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.dmg.gameconfigserver.model.bean.recharge.PlatformRechargeLogBean>
     * @author mice
     * @date 2019/12/27
    */
    IPage<PlatformRechargeLogBean> queryPlatfromRechargePage(PlatformRechargeDTO platformRechargeDTO);

    /**
     * @description: 查询问题订单
     * @param queryAbnormalOrderDTO
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.dmg.gameconfigserver.model.bean.recharge.PlatformRechargeLogBean>
     * @author mice
     * @date 2019/12/31
    */
    IPage<PlatformRechargeLogBean> queryAbnormalOrder(QueryAbnormalOrderDTO queryAbnormalOrderDTO);

    /**
     * @description: 补单
     * @param id
     * @param dealUserId
     * @return String
     * @author mice
     * @date 2019/12/31
    */
    String replenishOrder(Long id,Long dealUserId);
    
    /**
     * @Author liubo
     * @Description 今日首充人数//TODO
     * @Date 15:17 2020/3/17
     **/
    Long getFirstRechargeCount();

    /**
     * @Author liubo
     * @Description 今日充值金额//TODO
     * @Date 15:42 2020/3/17
     **/
    BigDecimal getSumRechargeAmount();

    /**
     * @Author liubo
     * @Description 今日首充金额//TODO
     * @Date 16:33 2020/3/17
     **/
    BigDecimal getSumFirstRechargeAmount();

}