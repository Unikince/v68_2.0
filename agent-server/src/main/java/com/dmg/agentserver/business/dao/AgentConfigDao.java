package com.dmg.agentserver.business.dao;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dmg.agentserver.business.model.po.AgentConfigPo;

/**
 * 代理配置
 */
@Mapper
public interface AgentConfigDao extends BaseMapper<AgentConfigPo> {
}
