package com.dmg.gameconfigserver.model.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author mice
 * @email .com
 * @date 2019-09-30 11:32:55
 */
@Data
@TableName("t_game_info")
public class GameInfoBean implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     *
     */
    private Integer gameId;
    /**
     *
     */
    private String gameName;
    /**
     *
     */
    private String gameCode;
    /**
     *
     */
    private Integer openStatus;
    /**
     * 游戏状态 游戏状态，0:维护,1:正常,2:流畅,3:拥挤,4:火爆
     */
    private Integer gameStatus;
    /**
     * 游戏类型 1:棋牌游戏 2:机台游戏 3:老虎机
     */
    private Integer gameType;
    /**
     * 路由id前缀
     */
    private String routeIdPrefix;

}
