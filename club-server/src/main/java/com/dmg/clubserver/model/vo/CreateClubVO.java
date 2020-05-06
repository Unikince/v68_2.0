package com.dmg.clubserver.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description
 * @Author mice
 * @Date 2019/5/27 17:57
 * @Version V1.0
 **/
@Data
public class CreateClubVO implements Serializable {
    /**
     * 俱乐部名称
     */
    private String name;
    /**
     * 创建人id
     */
    private Integer creatorId;
    /**
     * 俱乐部广告
     */
    private String remark;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 验证码
     */
    private String validateCode;
}