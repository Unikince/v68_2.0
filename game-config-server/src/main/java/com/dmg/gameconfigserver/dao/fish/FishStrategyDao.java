package com.dmg.gameconfigserver.dao.fish;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dmg.gameconfigserver.model.bean.fish.FishStrategyBean;

/**
 * 捕鱼刷鱼策略
 */
@Mapper
public interface FishStrategyDao extends BaseMapper<FishStrategyBean> {

}
