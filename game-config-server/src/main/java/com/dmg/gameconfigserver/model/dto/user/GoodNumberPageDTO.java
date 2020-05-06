package com.dmg.gameconfigserver.model.dto.user;

import lombok.Data;

import java.util.Date;

/**
 * @Description
 * @Author mice
 * @Date 2020/3/25 11:37
 * @Version V1.0
 **/
@Data
public class GoodNumberPageDTO {
    //id
    private Long id;
    // 靓号
    private Long goodNumber;
    // 玩家昵称
    private String nickname;
    //玩家id
    private Long userId;
    //更新时间
    private Date updateDate;
    //更新人名称
    private String updateUserName;

}