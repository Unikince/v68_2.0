/**
 * 
 */
package com.zyhy.common_lhj.process;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zyhy.common_server.result.HttpMessageResult;

/**
 * @author linanjun
 * 消息处理器
 */
public abstract class AbstractHttpMsgProcess implements IMsgProcess{

	private Logger log = LoggerFactory.getLogger(AbstractHttpMsgProcess.class);
	
	@SuppressWarnings("unchecked")
	public HttpMessageResult httpProcess(String uuid, Object body) {
			HttpMessageResult result = null;
			try {
				Map<String, String> message = (Map<String, String>)body;
				result = handler(uuid, message);
			} catch (Throwable e) {
				log.error("",e);
				result = new HttpMessageResult();
				result.setRet(2);
				result.setMsg("服务异常,uuid=" + uuid);
			}
			return result;
	}

	/**
	 * 
	 * @param uuid
	 * @param servercode
	 * @param body
	 * @return
	 * @throws Throwable
	 */
	public abstract HttpMessageResult handler(String uuid, Map<String, String> body) throws Throwable;
}
