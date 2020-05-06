package com.dmg.zhajinhuaserver.model.dto;

import lombok.Data;

import java.util.Date;

/**
 * @Description
 * @Author mice
 * @Date 2019/6/19 17:28
 * @Version V1.0
 **/
@Data
public class UserDTO {
    private Long id;
    /**
     * 用户code
     */
    private Long userCode;
    /**
     * 用户名称
     */
    private String userName;

    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 头像
     */
    private String headImage;
    /**
     * 账户余额
     */
    private double accountBalance;
    /**
     * 积分
     */
    private Long integral;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 生日
     */
    private Date birthday;
    /**
     * vip等级
     */
    private Integer vipLevel;
    /**
     * 是否是游客(0:不是 1:是)
     */
    private Integer tourist;

    private Integer sex;
}