package com.dmg.lobbyserver.model.dto;

import lombok.Data;

import java.util.Date;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 16:47 2020/3/23
 */
@Data
public class UserEmailDTO {

    private Long id;
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
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 是否领取
     */
    private Boolean receive;
    /**
     * 是否阅读
     */
    private Boolean hasRead;
}
