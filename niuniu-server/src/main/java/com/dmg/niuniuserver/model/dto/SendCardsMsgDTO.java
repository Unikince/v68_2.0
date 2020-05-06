package com.dmg.niuniuserver.model.dto;

import com.dmg.niuniuserver.model.bean.Poker;
import lombok.Data;

import java.util.List;

/**
 * @Description
 * @Author mice
 * @Date 2019/7/3 17:11
 * @Version V1.0
 **/
@Data
public class SendCardsMsgDTO {
    private Long userId;
    /**
     * 当前行动玩家位置
     */
    private Integer curActionSeatId;
    /**
     * 当前座位玩家手牌
     */
    private List<Poker> handCards;
    /**
     * 当前发牌次数
     */
    private Integer dealCount;
}