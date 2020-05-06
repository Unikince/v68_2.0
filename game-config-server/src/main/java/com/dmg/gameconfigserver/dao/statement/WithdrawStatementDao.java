package com.dmg.gameconfigserver.dao.statement;

import org.apache.ibatis.annotations.Param;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dmg.gameconfigserver.model.bean.user.UserBean;
import com.dmg.gameconfigserver.model.vo.statement.withdraw.WithdrawStatementCollectReq;
import com.dmg.gameconfigserver.model.vo.statement.withdraw.WithdrawStatementCollectRes;
import com.dmg.gameconfigserver.model.vo.statement.withdraw.WithdrawStatementDayDataReq;
import com.dmg.gameconfigserver.model.vo.statement.withdraw.WithdrawStatementDayDataRes;

/** 提款报表 */
@DS("v68")
public interface WithdrawStatementDao extends BaseMapper<UserBean> {// Bean对象用不上，随便指向该库中任意一个bean
    /** 汇总报表 */
    WithdrawStatementCollectRes collect(@Param("reqVo") WithdrawStatementCollectReq reqVo);

    /** 每日数据 */
    IPage<WithdrawStatementDayDataRes> dayData(Page<WithdrawStatementDayDataRes> page,@Param("reqVo") WithdrawStatementDayDataReq reqVo);
}
