package com.dmg.gameconfigserver.dao.bjl;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dmg.gameconfigserver.model.bean.bjl.BjlTableBean;

/**
 * 百家乐场次配置
 */
@Mapper
public interface BjlTableDao extends BaseMapper<BjlTableBean> {

}
