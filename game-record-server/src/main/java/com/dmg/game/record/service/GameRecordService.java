package com.dmg.game.record.service;

import com.dmg.game.record.model.GameRecordBean;
import com.dmg.game.record.model.dto.GameRecordQueryDTO;
import com.dmg.game.record.model.vo.GameRecordVO;

import java.util.List;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 11:59 2019/11/19
 */
public interface GameRecordService {


    /**
     * @Author liubo
     * @Description //TODO
     * @Date 13:34 2019/11/19
     **/
    Boolean insertGameRecord(GameRecordBean gameRecordBean);

    /**
     * @Author liubo
     * @Description //TODO 查询游戏记录
     * @Date 10:55 2019/11/29
     **/
    List<GameRecordVO> getGameRecordInfo(GameRecordQueryDTO gameRecordQueryDTO);
}
