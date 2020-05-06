package com.dmg.zhajinhuaserver.model.dto;

import lombok.Data;

/**
 * @Description
 * @Author mice
 * @Date 2019/7/3 18:18
 * @Version V1.0
 **/
@Data
public class QiangZhuangDTO {
    /**
     * 用户id
    */
    private Long userId;
    /**
     * 座位号
     */
    private Integer seatId;
    /**
     * 动作类型
     */
    private Integer actionType;
    /**
     * 倍数
     */
    private Integer multiple;
}