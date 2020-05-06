package com.dmg.game.task.model.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 10:39 2020/3/19
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_dmg_email")
public class EmailBean extends CommonBean implements Serializable {
    private static final long serialVersionUID = 1L;

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
    private Integer itemNum;
}
