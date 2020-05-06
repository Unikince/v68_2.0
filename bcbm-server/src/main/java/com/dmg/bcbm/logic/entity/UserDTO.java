package com.dmg.bcbm.logic.entity;


import java.util.Date;

/**
 * @Description
 * @Author mice
 * @Date 2019/6/19 17:28
 * @Version V1.0
 **/
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserCode() {
		return userCode;
	}

	public void setUserCode(Long userCode) {
		this.userCode = userCode;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getHeadImage() {
		return headImage;
	}

	public void setHeadImage(String headImage) {
		this.headImage = headImage;
	}

	public double getAccountBalance() {
		return accountBalance;
	}

	public void setAccountBalance(double accountBalance) {
		this.accountBalance = accountBalance;
	}

	public Long getIntegral() {
		return integral;
	}

	public void setIntegral(Long integral) {
		this.integral = integral;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Integer getVipLevel() {
		return vipLevel;
	}

	public void setVipLevel(Integer vipLevel) {
		this.vipLevel = vipLevel;
	}

	public Integer getTourist() {
		return tourist;
	}

	public void setTourist(Integer tourist) {
		this.tourist = tourist;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}
	@Override
	public String toString() {
		return "UserDTO [id=" + id + ", userCode=" + userCode + ", userName=" + userName + ", realName=" + realName
				+ ", headImage=" + headImage + ", accountBalance=" + accountBalance + ", integral=" + integral
				+ ", phone=" + phone + ", email=" + email + ", birthday=" + birthday + ", vipLevel=" + vipLevel
				+ ", tourist=" + tourist + ", sex=" + sex + "]";
	}
}