package com.dmg.gameconfigserver.model.dto.home;

import lombok.Data;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 16:36 2020/3/16
 */
@Data
public class GameOnlineRecordDTO {

    /**
     * 游戏id
     */
    private Integer gameId;
    /**
     * 场次id
     */
    private Integer fileId;
}
