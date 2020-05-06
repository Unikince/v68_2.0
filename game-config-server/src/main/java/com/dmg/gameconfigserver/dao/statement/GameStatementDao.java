package com.dmg.gameconfigserver.dao.statement;

import org.apache.ibatis.annotations.Param;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dmg.gameconfigserver.model.bean.user.UserBean;
import com.dmg.gameconfigserver.model.vo.statement.game.GameStatementCollectDataRes;
import com.dmg.gameconfigserver.model.vo.statement.game.GameStatementCollectReq;
import com.dmg.gameconfigserver.model.vo.statement.game.GameStatementDatailsCollectDayDataReq;
import com.dmg.gameconfigserver.model.vo.statement.game.GameStatementDatailsCollectDayDataRes;
import com.dmg.gameconfigserver.model.vo.statement.game.GameStatementDatailsCollectReq;
import com.dmg.gameconfigserver.model.vo.statement.game.GameStatementDatailsCollectRes;
import com.dmg.gameconfigserver.model.vo.statement.game.GameStatementDayDataDetailsReq;
import com.dmg.gameconfigserver.model.vo.statement.game.GameStatementDayDataDetailsRes;
import com.dmg.gameconfigserver.model.vo.statement.game.GameStatementDayDataReq;
import com.dmg.gameconfigserver.model.vo.statement.game.GameStatementDayDataRes;

/** 游戏报表 */
@DS("v68")
public interface GameStatementDao extends BaseMapper<UserBean> {// Bean对象用不上，随便指向该库中任意一个bean
    /** 汇总 */
    IPage<GameStatementCollectDataRes> collect(Page<GameStatementCollectDataRes> page, @Param("reqVo") GameStatementCollectReq reqVo);

    /** 每日数据 */
    IPage<GameStatementDayDataRes> dayData(Page<GameStatementDayDataRes> page, @Param("reqVo") GameStatementDayDataReq reqVo);

    /** 每日数据_游戏详情 */
    IPage<GameStatementDayDataDetailsRes> dayDataDetails(Page<GameStatementDayDataDetailsRes> page, @Param("reqVo") GameStatementDayDataDetailsReq reqVo);

    /** 游戏详情 */
    IPage<GameStatementDatailsCollectRes> datailsCollect(Page<GameStatementDatailsCollectRes> page, @Param("reqVo") GameStatementDatailsCollectReq reqVo);

    /** 游戏详情_每日数据 */
    IPage<GameStatementDatailsCollectDayDataRes> datailsCollectDayData(Page<GameStatementDatailsCollectDayDataRes> page, @Param("reqVo") GameStatementDatailsCollectDayDataReq reqVo);

}
