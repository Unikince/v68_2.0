package com.dmg.niuniuserver.model.dto;

import lombok.Data;

import java.util.List;

/**
 * @Description
 * @Author mice
 * @Date 2019/7/3 18:34
 * @Version V1.0
 **/
@Data
public class BankerInfoDTO {

    /**
     * 用户id
    */
    private Long userId;
    /**
     * 座位号
     */
    private Integer seatId;
    /**
     * 抢庄倍数
     */
    private Integer bankerMultiple;
    /**
     * 动作类型
     */
    private Integer actionType;
    /**
     * 当前行动的座位号列表
     */
    private List<Integer> curActionSeatIdList;

}