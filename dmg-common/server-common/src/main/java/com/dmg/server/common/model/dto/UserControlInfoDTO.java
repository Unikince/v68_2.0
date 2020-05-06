package com.dmg.server.common.model.dto;

import lombok.Data;

/**
 * 用户控制记录
 * @Description
 * @Author mice
 * @Date 2019/9/26 19:46
 * @Version V1.0
 **/
@Data
public class UserControlInfoDTO {
    /**
     * 玩家id
     */
    private Long userId;
    /**
     * 控制状态(1:点控,2:自控)
     */
    private Integer controlState;
    /**
     * 模型
     */
    private Integer model;
}