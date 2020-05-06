package com.dmg.gameconfigserver.dao.statement;

import org.apache.ibatis.annotations.Param;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dmg.gameconfigserver.model.bean.user.UserBean;
import com.dmg.gameconfigserver.model.vo.statement.player.PlayerChargeAndWithdrawInfo;
import com.dmg.gameconfigserver.model.vo.statement.player.PlayerStatementCollectReq;
import com.dmg.gameconfigserver.model.vo.statement.player.PlayerStatementDatailsCollectDayDataReq;
import com.dmg.gameconfigserver.model.vo.statement.player.PlayerStatementDatailsCollectDayDataRes;
import com.dmg.gameconfigserver.model.vo.statement.player.PlayerStatementDatailsCollectReq;
import com.dmg.gameconfigserver.model.vo.statement.player.PlayerStatementDatailsCollectRes;
import com.dmg.gameconfigserver.model.vo.statement.player.PlayerStatementDayDataDetailsReq;
import com.dmg.gameconfigserver.model.vo.statement.player.PlayerStatementDayDataDetailsRes;
import com.dmg.gameconfigserver.model.vo.statement.player.PlayerStatementDayDataReq;
import com.dmg.gameconfigserver.model.vo.statement.player.PlayerStatementDayDataRes;
import com.dmg.gameconfigserver.model.vo.statement.player.common.PlayerStatementCommonInfoRes;
import com.dmg.gameconfigserver.model.vo.statement.player.common.PlayerStatementCommonRes;

/** 玩家报表 */
@DS("v68")
public interface PlayerStatementDao extends BaseMapper<UserBean> {// Bean对象用不上，随便指向该库中任意一个bean
    /** 玩家详情获取玩家充值提款信息 */
    PlayerChargeAndWithdrawInfo getPlayerChargeAndWithdrawInfo(long playerId);

    /** 玩家报表 */
    IPage<PlayerStatementCommonInfoRes> collect(Page<PlayerStatementCommonRes> page, @Param("reqVo") PlayerStatementCollectReq reqVo);

    /** 每日数据 */
    IPage<PlayerStatementDayDataRes> dayData(Page<PlayerStatementDayDataRes> page, @Param("reqVo") PlayerStatementDayDataReq reqVo);

    /** 每日数据_游戏详情 */
    IPage<PlayerStatementDayDataDetailsRes> dayDataDetails(Page<PlayerStatementDayDataDetailsRes> page, @Param("reqVo") PlayerStatementDayDataDetailsReq reqVo);

    /** 游戏详情 */
    IPage<PlayerStatementDatailsCollectRes> datailsCollect(Page<PlayerStatementDatailsCollectRes> page, @Param("reqVo") PlayerStatementDatailsCollectReq reqVo);

    /** 游戏详情_每日数据 */
    IPage<PlayerStatementDatailsCollectDayDataRes> datailsCollectDayData(Page<PlayerStatementDatailsCollectDayDataRes> page, @Param("reqVo") PlayerStatementDatailsCollectDayDataReq reqVo);

}
