/**
 * 
 */
package com.zyhy.common_server.result;

import java.util.Map;

/**
 * @author linanjun
 * 客户端请求
 */
public class MessageClient {

	// 消息号
	private int messageid;
	// 用户所属平台id(1=Ebet,2=V68)
	private String uuid;
	// 登陆态
	private String sid;
	// 游戏类型
	private int type;
	// 参数
	private Map<String, String> params;
	
	public int getMessageid() {
		return messageid;
	}
	public void setMessageid(int messageid) {
		this.messageid = messageid;
	}
	public Map<String, String> getParams() {
		return params;
	}
	public void setParams(Map<String, String> params) {
		this.params = params;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "MessageClient [messageid=" + messageid + ", uuid=" + uuid + ", sid=" + sid + ", type=" + type
				+ ", params=" + params + "]";
	}
	
}
