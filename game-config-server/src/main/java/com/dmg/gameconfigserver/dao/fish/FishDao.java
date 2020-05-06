package com.dmg.gameconfigserver.dao.fish;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dmg.gameconfigserver.model.bean.fish.FishBean;

/**
 * 捕鱼-鱼
 */
@Mapper
public interface FishDao extends BaseMapper<FishBean> {

}
