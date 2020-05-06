package com.dmg.lobbyserver.model.vo;

import lombok.Data;

/**
 * @Description
 * @Author mice
 * @Date 2019/6/20 14:49
 * @Version V1.0
 **/
@Data
public class RecievePromotionCodeVO {
    private Long userId;
    private String promotionCode;
}