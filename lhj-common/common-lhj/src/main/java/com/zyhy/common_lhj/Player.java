/**
 * 
 */
package com.zyhy.common_lhj;

/**
 * @author ASUS
 *
 */
public class Player {

	/**
	 * 唯一标识
	 */
	private String roleId;
	
	/**
	 * 金币
	 */
	private double gold;
	
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

	public double getGold() {
		return gold;
	}

	public void setGold(double gold) {
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
		return "Player [roleId=" + roleId + ", gold=" + gold + ", nickname=" + nickname + ", tourist=" + tourist
				+ ", headImage=" + headImage + ", sex=" + sex + ", luck=" + luck + "]";
	}



	
}
