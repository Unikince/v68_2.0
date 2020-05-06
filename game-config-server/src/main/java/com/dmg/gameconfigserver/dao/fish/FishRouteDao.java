package com.dmg.gameconfigserver.dao.fish;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dmg.gameconfigserver.model.bean.fish.FishRouteBean;

/**
 * 捕鱼-鱼路线图
 */
@Mapper
public interface FishRouteDao extends BaseMapper<FishRouteBean> {

}
