package com.dmg.gameconfigserver.service.home;

import com.dmg.gameconfigserver.model.dto.home.GameOnlineRecordDTO;
import com.dmg.gameconfigserver.model.vo.home.GameFileOnlineDetailVO;
import com.dmg.gameconfigserver.model.vo.home.GameOnlineDetailVO;
import com.dmg.gameconfigserver.model.vo.home.GameOnlineRecordVO;

import java.util.List;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 15:39 2020/3/13
 */
public interface GameOnlineRecordService {
    /**
     * @Author liubo
     * @Description 查询折线图数据//TODO
     * @Date 16:37 2020/3/16
     **/
    List<GameOnlineRecordVO> getGameOnlineRecordList(GameOnlineRecordDTO gameOnlineRecordDTO);

    /**
     * @Author liubo
     * @Description //TODO
     * @Date 16:57 2020/3/16
     **/
    GameOnlineDetailVO getGameOnlineDetail();

    /**
     * @Author liubo
     * @Description //TODO
     * @Date 17:31 2020/3/16
     **/
    GameFileOnlineDetailVO getGameFileOnlineDetail(Integer gameId);

}
