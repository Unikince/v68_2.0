/**
 * 
 */
package com.dmg.bcbm.logic.entity;

import java.math.BigDecimal;

/**
 * @author ASUS
 *
 */
public class SimplePlayer {

	/**
	 * 唯一标识
	 */
	private String roleId;
	
	/**
	 * 金币
	 */
	private BigDecimal gold;
	
	/**
	 * 昵称
	 */
	private String nickname;
	// 账号类型
	private int tourist;
	 /** 头像 */
    private String headImage;
    /** 性别 */
    private int sex;
	
	/**
	 * 个人幸运值
	 * */
	private double luck;

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}


	public BigDecimal getGold() {
		return gold;
	}

	public void setGold(BigDecimal gold) {
		this.gold = gold;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	public double getLuck() {
		return luck;
	}

	public void setLuck(double luck) {
		this.luck = luck;
	}

	public int getTourist() {
		return tourist;
	}

	public void setTourist(int tourist) {
		this.tourist = tourist;
	}

	public String getHeadImage() {
		return headImage;
	}

	public void setHeadImage(String headImage) {
		this.headImage = headImage;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}


	@Override
	public String toString() {
		return "SimplePlayer [roleId=" + roleId + ", gold=" + gold + ", nickname=" + nickname + ", tourist=" + tourist
				+ ", headImage=" + headImage + ", sex=" + sex + ", luck=" + luck + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(gold.doubleValue());
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(luck);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((nickname == null) ? 0 : nickname.hashCode());
		result = prime * result + ((roleId == null) ? 0 : roleId.hashCode());
		result = prime * result + tourist;
		return result;
	}

	
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SimplePlayer other = (SimplePlayer) obj;
		if (Double.doubleToLongBits(gold.doubleValue()) != Double.doubleToLongBits(other.gold.doubleValue()))
			return false;
		if (Double.doubleToLongBits(luck) != Double.doubleToLongBits(other.luck))
			return false;
		if (nickname == null) {
			if (other.nickname != null)
				return false;
		} else if (!nickname.equals(other.nickname))
			return false;
		if (roleId == null) {
			if (other.roleId != null)
				return false;
		} else if (!roleId.equals(other.roleId))
			return false;
		if (tourist != other.tourist)
			return false;
		return true;
	}


	
}
