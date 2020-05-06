package com.dmg.gameconfigserverapi.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 9:40 2020/4/2
 */
@Data
public class UserEmailDTO {

    /**
     * 转账记录id
     */
    private Long transferAccountId;
    /**
     * 邮件名称
     */
    private String emailName;
    /**
     * 邮件内容信息
     */
    private String emailContent;
    /**
     * 过期时间
     */
    private Date expireDate;
    /**
     * 发送时间
     */
    private Date sendDate;
    /**
     * 物品类型
     */
    private Integer itemType;
    /**
     * 物品数量
     */
    private BigDecimal itemNum;
    /**
     * 用户id
     */
    private Long userId;
}
