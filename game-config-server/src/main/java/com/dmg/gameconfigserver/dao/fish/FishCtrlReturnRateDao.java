package com.dmg.gameconfigserver.dao.fish;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dmg.gameconfigserver.model.bean.fish.FishCtrlReturnRateBean;

/**
 * 捕鱼返奖率控制
 */
@Mapper
public interface FishCtrlReturnRateDao extends BaseMapper<FishCtrlReturnRateBean> {
}
