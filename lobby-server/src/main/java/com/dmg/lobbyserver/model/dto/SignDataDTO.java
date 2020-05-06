package com.dmg.lobbyserver.model.dto;

import lombok.Data;

/**
 * 签到数据列表
 * Author:刘将军
 * Time:2019/6/20 15:40
 * Created by IntelliJ IDEA Community
 */
@Data
public class SignDataDTO {
    /**
     * 第几天
     */
    private Integer orderNum;
    /**
     * 该用户是否已经签到
     */
    private boolean signed;
    /**
     * 物品名称
     */
    private String itemName;
    /**
     * 小图片
     */
    private String img;
    /**
     * 物品数量
     */
    private Integer num;
}
