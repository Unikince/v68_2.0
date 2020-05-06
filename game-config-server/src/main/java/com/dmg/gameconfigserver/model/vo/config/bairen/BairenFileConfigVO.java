package com.dmg.gameconfigserver.model.vo.config.bairen;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @Description
 * @Author mice
 * @Date 2019/10/10 10:01
 * @Version V1.0
 **/
@Data
public class BairenFileConfigVO {
    /**
     * 基础配置id
     */
    private Integer fileBaseConfigId;
    /**
     * 准入限制
     */
    @NotNull(message = "fileLimit不能为空")
    private Integer fileLimit;
    /**
     * 房间下注条件下限
     */
    @NotNull(message = "roomBetLowLimit不能为空")
    private Integer roomBetLowLimit;
    /**
     * 下注下限
     */
    @NotNull(message = "redValue不能为空")
    private Integer redValue;
    /**
     * 区域下注下限
     */
    @NotNull(message = "areaBetLowLimit不能为空")
    private Long areaBetLowLimit;
    /**
     * 区域下注上限
     */
    @NotNull(message = "areaBetUpLimit不能为空")
    private Long areaBetUpLimit;
    /**
     * 上庄下限
     */
    @NotNull(message = "applyBankerLimit不能为空")
    private Integer applyBankerLimit;
    /**
     * 自动下庄下限
     */
    @NotNull(message = "bankerGoldLowLimit不能为空")
    private Integer bankerGoldLowLimit;
    /**
     * 连庄局数限制
     */
    @NotNull(message = "bankRoundLimit不能为空")
    private Integer bankRoundLimit;
    /**
     * 观战局数限制
     */
    @NotNull(message = "watchRoundLimit不能为空")
    private Integer watchRoundLimit;
    /**
     * 下注筹码
     */
    @org.hibernate.validator.constraints.NotEmpty(message = "betChips不能为空")
    private String betChips;

    /**
     * 游戏id
     */
    @NotNull(message = "gameId不能为空")
    private Integer gameId;
    /**
     * 场次id
     */
    @NotNull(message = "fileId不能为空")
    private Integer fileId;
    /**
     * 场次名称
     */
    @NotEmpty(message = "fileName不能为空")
    private String fileName;
    /**
     * 是否开启 0:未开启 1:已开启
     */
    @NotNull(message = "openStatus不能为空")
    private Integer openStatus;
    /**
     * 人数上限
     */
    @NotNull(message = "playerUpLimit不能为空")
    private Integer playerUpLimit;
    /**
     * 抽水百分比
     */
    @NotNull(message = "pumpRate不能为空")
    private BigDecimal pumpRate;
}