package com.dmg.lobbyserver.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description
 * @Author mice
 * @Date 2019/6/26 14:44
 * @Version V1.0
 **/
@Data
public class LoginV68GameVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    /**
     * 用户名称
     */
    private String userName;
    /**
     * 头像
     */
    private String headImage;
    /**
     * 账户余额
     */
    private BigDecimal accountBalance;
    /**
     * 手机号
     */
    private String phone;
}