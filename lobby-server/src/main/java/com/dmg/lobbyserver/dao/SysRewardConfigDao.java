package com.dmg.lobbyserver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dmg.lobbyserver.dao.bean.SysItemConfigBean;
import com.dmg.lobbyserver.dao.bean.SysRewardConfigBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 系统奖励配置
 *
 * @author mice
 * @email .com
 * @date 2019-06-19 15:40:44
 */
@Mapper
public interface SysRewardConfigDao extends BaseMapper<SysRewardConfigBean> {

    @Select(" select * from sys_reward_config where id in (${ids})")
    List<SysRewardConfigBean> getRewards(@Param("ids") String ids);

}
