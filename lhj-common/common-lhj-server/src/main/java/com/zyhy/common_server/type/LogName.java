/**
 * 
 */
package com.zyhy.common_server.type;

/**
 * @author linanjun
 *
 */
public enum LogName {
	
	GAME_LOG("游戏日志"),
	INGOT_LOG("货币日志"),
	USER_LOG("用户日志"),
	ERROR_LOG("错误日志"),
	MESSAGE_LOG("消息日志"),
	MANAGEUSER_LOG("管理员日志"),
	BUSINESS_LOG("商户日志"),
	CLIENTERROR_LOG("客户端错误日志"),
	GAME_LHJ_LOG("老虎机游戏日志"),
	USER_LHJ_LOG("老虎机用户日志"),
	;
	
	private LogName(String value){
		this.value = value;
	}
	private String value;
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
