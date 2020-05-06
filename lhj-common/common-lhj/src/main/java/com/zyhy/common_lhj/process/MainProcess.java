/**
 * 
 */
package com.zyhy.common_lhj.process;

import java.util.Map;

import com.zyhy.common_server.result.HttpMessageResult;
import com.zyhy.common_server.result.MessageClient;

/**
 * @author linanjun
 * 消息入口
 */
public class MainProcess {

	// 消息列表
	private Map<Integer, IMsgProcess> httpHandlerMapping;
	
	/**
	* 
	* @Title: mainProcessMsg 
	* @Description: http消息处理
	* @param @param c
	* @param @return
	* @return Object    返回类型 
	* @throws
	 */
	public HttpMessageResult mainProcessMsg(MessageClient c){
		IMsgProcess messageProcess = httpHandlerMapping.get(c.getMessageid());
		if (messageProcess != null) {
			HttpMessageResult result = null;
			try {
				result = messageProcess.httpProcess(c.getUuid(), c.getParams());
			} catch (Exception e) {
				e.printStackTrace();
				result = new HttpMessageResult();
				result.setRet(2);
				result.setMsg("网络异常");
			}
			return result;
		}else {
			HttpMessageResult result = new HttpMessageResult();
			result.setRet(2);
			result.setMsg("消息号没找到=" + c.getMessageid());
			return result;
		}
	}

	public void setHttpHandlerMapping(Map<Integer, IMsgProcess> httpHandlerMapping) {
		this.httpHandlerMapping = httpHandlerMapping;
	}

}
