package com.dmg.gameconfigserver.model.bean.record;

import com.baomidou.mybatisplus.annotation.TableName;
import com.dmg.gameconfigserver.model.bean.CommonBean;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 13:47 2019/11/18
 */
@Data
@TableName("t_dmg_game_record")
public class GameRecordBean extends CommonBean implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 游戏牌局时间
     */
    private Date gameDate;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 用户昵称
     */
    private String userName;
    /**
     * 游戏id
     */
    private Integer gameId;
    /**
     * 游戏名称
     */
    private String gameName;
    /**
     * 场次id
     */
    private Integer fileId;
    /**
     * 场次名称
     */
    private String fileName;
    /**
     * 游戏底分
     */
    private BigDecimal baseScore;
    /**
     * 游戏角色
     */
    private String role;
    /**
     * 叫分
     */
    private Integer callBranch;
    /**
     * 倍数
     */
    private String multiples;
    /**
     * 游戏结果
     */
    private String gameResult;
    /**
     * 游戏前金币
     */
    private BigDecimal beforeGameGold;
    /**
     * 游戏后金币
     */
    private BigDecimal afterGameGold;
    /**
     * 下注金币
     */
    private BigDecimal betsGold;
    /**
     * 输赢金币
     */
    private BigDecimal winLosGold;
    /**
     * 服务费
     */
    private BigDecimal serviceCharge;
    /**
     * 是否機器人
     */
    private Boolean isRobot;
    /**
     * 牌局编号
     */
    private String roundCode;

}
