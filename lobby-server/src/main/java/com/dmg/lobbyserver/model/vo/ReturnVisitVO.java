package com.dmg.lobbyserver.model.vo;

import lombok.Data;

/**
 * @Description
 * @Author mice
 * @Date 2019/6/24 16:49
 * @Version V1.0
 **/
@Data
public class ReturnVisitVO {
    private Long userId;
    private String phone;
    private String validateCode;
}