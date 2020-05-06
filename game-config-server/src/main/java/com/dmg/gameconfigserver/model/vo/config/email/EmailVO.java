package com.dmg.gameconfigserver.model.vo.config.email;

import lombok.Data;

import java.util.Date;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 11:31 2020/3/19
 */
@Data
public class EmailVO {

    private Long id;

    /**
     * 修改时间
     */
    private Date modifyDate;
    /**
     * 修改人
     */
    private Long modifyUser;

    private String nickName;

    /**
     * userIds
     */
    private String userIds;
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
    private String itemNum;

    /**
     * 发送状态
     */
    private Integer sendStatus;
}
