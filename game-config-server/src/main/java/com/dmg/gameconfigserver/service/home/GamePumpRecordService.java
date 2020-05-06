package com.dmg.gameconfigserver.service.home;

import com.dmg.gameconfigserver.model.dto.home.GamePumpRecordDTO;
import com.dmg.gameconfigserver.model.vo.home.GameFilePumpDetailVO;
import com.dmg.gameconfigserver.model.vo.home.GamePumpDetailVO;
import com.dmg.gameconfigserver.model.vo.home.GamePumpRecordVO;

import java.util.List;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 15:39 2020/3/13
 */
public interface GamePumpRecordService {

    /**
     * @Author liubo
     * @Description 查询折线图数据//TODO
     * @Date 16:37 2020/3/16
     **/
    List<GamePumpRecordVO> getGamePumpRecordList(GamePumpRecordDTO gamePumpRecordDTO);

    /**
     * @Author liubo
     * @Description //TODO 
     * @Date 16:57 2020/3/16
     **/
    GamePumpDetailVO getGamePumpDetail();

    /**
     * @Author liubo
     * @Description //TODO
     * @Date 17:31 2020/3/16
     **/
    GameFilePumpDetailVO getGameFilePumpDetail(Integer gameId);
}
