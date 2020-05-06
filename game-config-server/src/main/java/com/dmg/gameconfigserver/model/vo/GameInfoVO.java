package com.dmg.gameconfigserver.model.vo;

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

    @NotNull(message = "id不能为空")
    private Integer id;

    private Integer gameId;

    private String gameName;
    /**
     * 游戏状态
     */
    @NotNull(message = "gameStatus不能为空")
    private Integer gameStatus;
    private Long operatorId;
}