package com.dmg.lobbyserver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dmg.lobbyserver.dao.bean.SysItemConfigBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 系统物品配置表
 * 
 * @author mice
 * email .com
 * date 2019-06-18 17:31:03
 */
@Mapper
public interface SysItemConfigDao extends BaseMapper<SysItemConfigBean> {
@Select(" select * from sys_item_config where id in (${ids})")
    List<SysItemConfigBean>  getSystemItems(@Param("ids")String  ids );
}
