package com.dmg.lobbyserver.dao.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户表
 * 
 * @author mice
 */
@Data
@TableName("t_dmg_user")
public class UserBean implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId(type = IdType.INPUT)
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
	 * 用户类型 1:正式玩家 2:测试玩家 3:代理
	 */
	private Integer userType;
	/**
	 * 密码
	 */
	private String password;
	/**
	 * 真实姓名
	 */
	private String realName;
	/**
	 * 头像
	 */
	private String headImage;
	/**
	 * 微信openID
	 */
	private String openId;
	/**
	 * 账户状态 1:正常 0:禁用
	 */
	private Integer accountStatus;
	/**
	 * 账户余额
	 */
	private BigDecimal accountBalance;
	/**
	 * 保险箱余额
	 */
	private BigDecimal strongboxBalance;
	/**
	 * 保险箱密码
	 */
	private String strongboxPassword;
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
	 * 设备编码
	 */
	private String deviceCode;
	/**
	 * 是否是游客(0:不是 1:是)
	 */
	private Integer tourist;
	/**
	 * 是否是新用户(0:不是 1:是)
	 */
	private Integer newUser;
	/**
	 * 指纹key
	 */
	private String fingerprintKey;
	/**
	 * 面部key
	 */
	private String faceKey;
	/**
	 * 手势key
	 */
	private String gestureKey;
	/**
	 * 创建日期
	 */
	private Date createDate;
	/**
	 * 修改日期
	 */
	private Date updateDate;
	private Integer sex;
    /**
     * 登录日期
     */
    private Date loginDate;
    /**
     * 注册日期
     */
    private String registerIp;
	/**
	 * 描述
	 */
	private String remark;
	/**
	 * 靓号
	 */
	private Long goodNumber;
	/**
	 * 渠道code
	 */
	private String channelCode;
}
