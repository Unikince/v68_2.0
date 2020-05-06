package com.dmg.gameconfigserver.model.bean.config;

import java.io.Serializable;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableName;
import com.dmg.gameconfigserver.model.bean.CommonBean;

import lombok.Data;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 14:04 2019/10/11
 */
@Data
@TableName("t_dmg_game_file_config")
public class GameFileBean extends CommonBean implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 基础配置id
     */
    private Integer fileBaseConfigId;
    /**
     * 底分
     */
    private BigDecimal baseScore;
    /**
     * 玩家携带下限
     */
    private BigDecimal lowerLimit;
    /**
     * 玩家携带上限
     */
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
    private Integer readyTime;
}
