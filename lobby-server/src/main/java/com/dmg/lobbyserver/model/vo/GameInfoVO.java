package com.dmg.lobbyserver.model.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Description
 * @Author mice
 * @Date 2019/9/27 14:44
 * @Version V1.0
 **/
@Data
public class GameInfoVO {
    /**
     * id
     */
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
     * 游戏状态
     */
    private Integer gameStatus;
}