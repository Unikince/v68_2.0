package com.dmg.agentserver.business.dao;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dmg.agentserver.business.model.po.AgentLevelPo;

/**
 * 代理等级
 */
@Mapper
public interface AgentLevelDao extends BaseMapper<AgentLevelPo> {

}
