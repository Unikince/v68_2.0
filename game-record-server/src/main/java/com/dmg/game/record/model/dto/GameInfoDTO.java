package com.dmg.game.record.model.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Description
 * @Author mice
 * @Date 2019/9/27 14:44
 * @Version V1.0
 **/
@Data
public class GameInfoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer gameId;

    private String gameName;
    /**
     * 游戏状态
     */
    private Integer gameStatus;
}