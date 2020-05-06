/**
 * 
 */
package com.zyhy.common_server.result;

import com.zyhy.common_server.result.HttpMessageResult;

/**
 * @author lnj
 *
 */
public class TcpMessageResult extends HttpMessageResult{
	
	// 消息号
	private int messageid;
		
	public TcpMessageResult(int messageid){
		this.messageid = messageid;
	}
	
	public int getMessageid() {
		return messageid;
	}
	public void setMessageid(int messageid) {
		this.messageid = messageid;
	}
}
