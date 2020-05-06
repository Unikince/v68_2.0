package com.dmg.gameconfigserver.dao.statement;

import org.apache.ibatis.annotations.Param;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dmg.gameconfigserver.model.bean.user.UserBean;
import com.dmg.gameconfigserver.model.vo.statement.recharge.RechargeStatementCollectReq;
import com.dmg.gameconfigserver.model.vo.statement.recharge.RechargeStatementCollectRes;
import com.dmg.gameconfigserver.model.vo.statement.recharge.RechargeStatementDatailsCollectDayDataReq;
import com.dmg.gameconfigserver.model.vo.statement.recharge.RechargeStatementDatailsCollectDayDataRes;
import com.dmg.gameconfigserver.model.vo.statement.recharge.RechargeStatementDatailsCollectReq;
import com.dmg.gameconfigserver.model.vo.statement.recharge.RechargeStatementDatailsCollectRes;
import com.dmg.gameconfigserver.model.vo.statement.recharge.RechargeStatementDayDataReq;
import com.dmg.gameconfigserver.model.vo.statement.recharge.RechargeStatementDayDataRes;

/** 充值报表 */
@DS("v68")
public interface RechargeStatementDao extends BaseMapper<UserBean> {// Bean对象用不上，随便指向该库中任意一个bean
    /** 汇总-人工充值 */
    RechargeStatementCollectRes collectByPersion(@Param("reqVo") RechargeStatementCollectReq reqVo);

    /** 汇总-渠道充值 */
    RechargeStatementCollectRes collectByPlatform(@Param("reqVo") RechargeStatementCollectReq reqVo);

    /** 每日数据 */
    IPage<RechargeStatementDayDataRes> dayDataByPersion(Page<RechargeStatementDayDataRes> page, @Param("reqVo") RechargeStatementDayDataReq reqVo);

    /** 每日数据 */
    IPage<RechargeStatementDayDataRes> dayDataByPlatform(Page<RechargeStatementDayDataRes> page, @Param("reqVo") RechargeStatementDayDataReq reqVo);

    /** 游戏详情 */
    IPage<RechargeStatementDatailsCollectRes> datailsCollectByPlatform(Page<RechargeStatementDatailsCollectRes> page, @Param("reqVo") RechargeStatementDatailsCollectReq reqVo);

    /** 游戏详情_每日数据 */
    IPage<RechargeStatementDatailsCollectDayDataRes> datailsCollectDayDataByPlatform(Page<RechargeStatementDatailsCollectDayDataRes> page, @Param("reqVo") RechargeStatementDatailsCollectDayDataReq reqVo);

}
