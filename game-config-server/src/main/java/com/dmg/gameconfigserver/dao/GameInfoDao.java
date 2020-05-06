package com.dmg.gameconfigserver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dmg.gameconfigserver.model.bean.GameInfoBean;
import com.dmg.gameconfigserver.model.vo.GameFileVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 
 * 
 * @author mice
 * @email .com
 * @date 2019-09-30 11:32:55
 */
@Mapper
public interface GameInfoDao extends BaseMapper<GameInfoBean> {

    List<GameFileVO> getGameFile();

}
