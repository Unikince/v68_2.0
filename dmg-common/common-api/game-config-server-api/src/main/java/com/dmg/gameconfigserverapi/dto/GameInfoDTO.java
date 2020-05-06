package com.dmg.gameconfigserverapi.dto;

import lombok.Data;

/**
 * @Description
 * @Author mice
 * @Date 2019/10/17 11:15
 * @Version V1.0
 **/
@Data
public class GameInfoDTO {

    private Integer id;
    /**
     * 游戏id
     */
    private Integer gameId;
    /**
     * 游戏名称
     */
    private String gameName;
    /**
     * 游戏code
     */
    private String gameCode;
    /**
     * 开放状态
     */
    private Integer openStatus;
}