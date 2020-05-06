package com.dmg.lobbyserver.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 礼包物品数据DTO
 * Author:刘将军
 * Time:2019/6/20 10:36
 * Created by IntelliJ IDEA Community
 */

@Data
public class GiftDataDTO implements Serializable {
    /**
     * 物品名称
     */
    private String itemName;
    /**
     * 物品描述
     */
    private String remark;
    /**
     * 物品图片id(小)
     */
    private String smallPicId;
    /**
     * 物品数量
     */
    private String itemNumber;
}
