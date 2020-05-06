package com.dmg.zhajinhuaserver.model.dto;

import com.dmg.zhajinhuaserver.model.bean.Poker;
import lombok.Data;

import java.util.List;

/**
 * @Description
 * @Author mice
 * @Date 2019/7/3 17:57
 * @Version V1.0
 **/
@Data
public class KaiPaiDTO {
    private Long userId;
    /**
     * 操作类型
     */
    private Integer actionType;
    /**
     * 座位号
    */
    private Integer seatId;
    /**
     * 手牌类型 参看枚举:Combination
     */
    private Integer handCardType;
    /**
     * 玩家拼整数牌型列表 3张
     */
    private List<Poker> allCardTypeList;
    /**
     * 玩家散牌列表 2张
     */
    private List<Poker> scatteredCardList;
    /**
     * 玩家手牌
     */
    private List<Poker> handCardList;
}