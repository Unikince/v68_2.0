package com.dmg.lobbyserver.model.dto;

import lombok.Data;


/**
 * @Description
 * @Author jock
 * @Date 2019/7/4 0004
 * @Version V1.0
 **/
@Data
public class TaskItemDetailsDTO {
    /**
     * 奖励说明
     */
    private String ItemDesc;
    /**
     * 奖励数量
     */
    private Integer num;
    /**
     * 物品名称
     */
    private String itemName;
    /**
     * 物品类型
     */
    private Integer itemType;
    /**
     * 物品图片id(小)
     */
    private String smallPicId;
    /**
     * 物品图片id(大)
     */
    private String bigPicId;
}
