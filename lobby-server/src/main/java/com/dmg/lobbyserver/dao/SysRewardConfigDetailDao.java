package com.dmg.lobbyserver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dmg.lobbyserver.dao.bean.SysRewardConfigBean;
import com.dmg.lobbyserver.dao.bean.SysRewardConfigDetailBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 系统奖励配置
 * 
 * @author mice
 * @email .com
 * @date 2019-06-19 15:40:43
 */
@Mapper
public interface SysRewardConfigDetailDao extends BaseMapper<SysRewardConfigDetailBean> {

    @Select("select * from sys_reward_config_detail where id in (${ids})")
    List<SysRewardConfigDetailBean> getSysRewards(@Param("ids") String ids);
}
