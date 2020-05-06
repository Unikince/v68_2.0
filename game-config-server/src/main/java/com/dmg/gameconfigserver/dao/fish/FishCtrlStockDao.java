package com.dmg.gameconfigserver.dao.fish;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dmg.gameconfigserver.model.bean.fish.FishCtrlStockBean;

/**
 * 捕鱼库存控制
 */
@Mapper
public interface FishCtrlStockDao extends BaseMapper<FishCtrlStockBean> {
}
