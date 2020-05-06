/**
 * 
 */
package com.dmg.clubserver.model;

import com.zyhy.common_server.constants.ZoneTimeUTC;
import com.zyhy.common_server.util.DateUtils;
import com.zyhy.common_server.util.JsonUtils;

/**
 * @author linanjun 公告信息
 */
public class PushBarInfo {

	// 编号
	private long code;
	// 公告类型 1=系统,2=游戏
	private int type;
	// 内容
	private String content;
	// 推送时间
	private long pushtime;

	public PushBarInfo() {
	}

	public String toString0() {
		return JsonUtils.object2json(this);
	}

	/**
	 * @param code
	 * @param type
	 * @param content
	 */
	public PushBarInfo(long code, int type, String content) {
		super();
		this.code = code;
		this.type = type;
		this.content = content;
		this.pushtime = DateUtils.getEpochMilliByZoneUTC(ZoneTimeUTC.BEIJING_UTC);
	}

	/**
	 * @return the code
	 */
	public long getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(long code) {
		this.code = code;
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the pushtime
	 */
	public long getPushtime() {
		return pushtime;
	}

	/**
	 * @param pushtime the pushtime to set
	 */
	public void setPushtime(long pushtime) {
		this.pushtime = pushtime;
	}

}
