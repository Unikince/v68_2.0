package com.dmg.gameconfigserver.dao.home;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dmg.gameconfigserver.model.bean.home.GamePumpRecordBean;
import com.dmg.gameconfigserver.model.vo.home.GamePumpRecordVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 15:38 2020/3/13
 */
public interface GamePumpRecordDao extends BaseMapper<GamePumpRecordBean> {

    List<GamePumpRecordVO> getGamePumpRecordList(@Param("gameId") Integer gameId,
                                                 @Param("fileId") Integer fileId);
}
