package com.dmg.gameconfigserver.model.bean.config;

import com.baomidou.mybatisplus.annotation.TableName;
import com.dmg.gameconfigserver.model.bean.CommonBean;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 11:47 2019/10/14
 */
@Data
@TableName("t_dmg_game_control_config")
public class GameControlBean extends CommonBean implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 基础配置id
     */
    private Integer fileBaseConfigId;
    /**
     * 发牌权重最小值
     */
    private Integer minDealCards;
    /**
     * 发牌权重最大值
     */
    private Integer maxDealCards;
    /**
     * 跟注金额最小值
     */
    private Integer minFollowUp;
    /**
     * 跟注金额最大值
     */
    private Integer maxFollowUp;
    /**
     * 炸弹个数最小值
     */
    private Integer minBomb;
    /**
     * 炸弹个数最小值
     */
    private Integer maxBomb;
    /**
     * 权重定义
     */
    private String weight;
}
