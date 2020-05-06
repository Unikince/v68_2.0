package com.dmg.gameconfigserver.dao.fish;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dmg.gameconfigserver.model.bean.fish.FishRoomBean;

/**
 * 捕鱼房间配置
 */
@Mapper
public interface FishRoomDao extends BaseMapper<FishRoomBean> {

}
