package com.dmg.gameconfigserver.dao.fish;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dmg.gameconfigserver.model.bean.fish.FishScenceBean;

/**
 * 捕鱼场景
 */
@Mapper
public interface FishScenceDao extends BaseMapper<FishScenceBean> {

}
