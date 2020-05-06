package com.dmg.lobbyserver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dmg.lobbyserver.dao.bean.SysRechargeLogBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Description  充值配置
 * @Author jock
 * @Date 2019/7/3 0003
 * @Version V1.0
 **/
@Mapper
public interface SysRechargeLogDao extends BaseMapper<SysRechargeLogBean> {
    /**
     * 充值金额显示
     */
    @Select("select * from sys_recharge_log order by recharge_number  desc")
    List<SysRechargeLogBean> getMoneyList();
}
