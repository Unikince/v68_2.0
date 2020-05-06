package com.dmg.gameconfigserver.model.vo.config;

import com.dmg.gameconfigserver.model.vo.group.SaveValid;
import com.dmg.gameconfigserver.model.vo.group.UpdateValid;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 14:31 2019/10/14
 */
@Data
public class GameControlVO {
    private Long id;
    /**
     * 基础配置id
     */
    @NotNull(message = "fileBaseConfigId不能为空", groups = {SaveValid.class, UpdateValid.class})
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
