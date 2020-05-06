package com.dmg.agentserver.business.dao;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dmg.agentserver.business.model.po.PerformanceConfigPo;

/**
 * 业绩配置
 */
@Mapper
public interface PerformanceConfigDao extends BaseMapper<PerformanceConfigPo> {

}
