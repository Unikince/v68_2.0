/**
 * 
 */
package com.zyhy.lhj_server.bgmanagement.prcess.http;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.zyhy.common_lhj.process.AbstractHttpMsgProcess;
import com.zyhy.common_server.result.HttpMessageResult;
import com.zyhy.lhj_server.bgmanagement.constants.SoltMessageConstants;
import com.zyhy.lhj_server.bgmanagement.dao.imp.BgManagementDaoImp;
import com.zyhy.lhj_server.bgmanagement.entity.GamePoolConfig;

/**
 * 查询单个游戏信息
 */
@Order
//@Component
public class queryPoolConfigProcess extends AbstractHttpMsgProcess{
	@Autowired
	private BgManagementDaoImp bgManagementDaoImp;
	
	@Override
	public int getMessageId() {
		return SoltMessageConstants.QUERYPOOLCONFIG;
	}

	@Override
	public HttpMessageResult handler(String uuid, Map<String, String> body)
			throws Throwable {
		HttpMessageResult result = new HttpMessageResult();
		int gameId = Integer.parseInt(body.get("gameId"));
		String poolName = body.get("poolName");
		GamePoolConfig query = bgManagementDaoImp.querySinglePoolConfig(gameId, poolName);
		if (query != null) {
			result.setMsg(JSONObject.toJSONString(query));
		} else {
			result.setRet(2);
			result.setMsg("queryPoolConfig is empty!");
		}
		return result;
	}

}
