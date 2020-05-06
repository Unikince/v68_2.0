package com.dmg.gameconfigserver.model.vo.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description
 * @Author mice
 * @Date 2019/11/20 14:39
 * @Version V1.0
 **/
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserVO {

    @NotNull(message = "id不能为空")
    private Long id;
    /**
     * 注册日期
     */
    private Date registerDate;

    /**
     * 用户code
     */
    private Long userCode;
    /**
     * 用户名称
     */
    private String userName;
    /**
     * 密码
     */
    private String password;
    /**
     * 账户状态 1:正常 2:禁用
     */
    private Integer accountStatus;
    /**
     * 账户余额
     */
    private BigDecimal accountBalance;
    /**
     * 保险箱余额
     */
    private Double strongboxBalance;
    /**
     * 保险箱密码
     */
    private String strongboxPassword;
    /**
     * 手机号
     */
    private String phone;
    /**
     * vip等级
     */
    private Integer vipLevel;
    /**
     * 登录日期
     */
    private Date loginDate;
    /**
     * 注册Ip
     */
    private String registerIp;
    /**
     * 描述
     */
    private String remark;
    /**
     * 是否邦卡
     */
    private Boolean bindingCard = false;
    /**
     * 是否在线
     */
    private Boolean isOnline = false;
    /**
     * 用户类型 1:正式玩家 2:测试玩家 3:代理
     */
    private Integer userType;

}