package com.dmg.gameconfigserver.model.vo.config;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import com.dmg.gameconfigserver.model.vo.group.SaveValid;
import com.dmg.gameconfigserver.model.vo.group.UpdateValid;

import lombok.Data;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 13:53 2019/10/14
 */
@Data
public class GameFileConfigVO {
    private Long id;
    /**
     * 游戏id
     */
    @NotNull(message = "gameId不能为空", groups = SaveValid.class)
    private Integer gameId;
    /**
     * 场次id
     */
    @NotNull(message = "fildId不能为空", groups = SaveValid.class)
    private Integer fileId;
    /**
     * 场次名称
     */
    @NotNull(message = "fileName不能为空", groups = SaveValid.class)
    private String fileName;
    /**
     * 是否开启 0:未开启 1:已开启
     */
    @NotNull(message = "openStatus不能为空", groups = SaveValid.class)
    private Integer openStatus;
    /**
     * 人数上限
     */
    @NotNull(message = "playerUpLimit不能为空", groups = SaveValid.class)
    private Integer playerUpLimit;
    /**
     * 抽水百分比
     */
    @NotNull(message = "pumpRate不能为空", groups = SaveValid.class)
    private BigDecimal pumpRate;

    /**
     * 基础配置id
     */
    @NotNull(message = "fileBaseConfigId不能为空", groups = UpdateValid.class)
    private Integer fileBaseConfigId;
    /**
     * 底分
     */
    @NotNull(message = "baseScore不能为空", groups = SaveValid.class)
    private BigDecimal baseScore;
    /**
     * 玩家携带下限
     */
    @NotNull(message = "lowerLimit不能为空", groups = SaveValid.class)
    private BigDecimal lowerLimit;
    /**
     * 玩家携带上限
     */
    @NotNull(message = "upperLimit不能为空", groups = SaveValid.class)
    private BigDecimal upperLimit;
    /**
     * 轮数
     */
    private Integer roundMax;
    /**
     * 加注上限
     */
    private String betChips;
    /**
     * 倍数上限
     */
    private String discards;
    /**
     * 不准备踢出时间
     */
    @NotNull(message = "readyTime不能为空", groups = SaveValid.class)
    private Integer readyTime;
}
