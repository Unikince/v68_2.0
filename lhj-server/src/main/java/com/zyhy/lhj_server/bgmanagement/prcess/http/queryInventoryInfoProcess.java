/**
 * 
 */
package com.zyhy.lhj_server.bgmanagement.prcess.http;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.zyhy.common_lhj.process.AbstractHttpMsgProcess;
import com.zyhy.common_server.result.HttpMessageResult;
import com.zyhy.lhj_server.bgmanagement.constants.SoltMessageConstants;
import com.zyhy.lhj_server.bgmanagement.dao.imp.BgManagementDaoImp;
import com.zyhy.lhj_server.bgmanagement.entity.SoltGameInventoryConfig;

/**
 * 查询游戏库存配置
 */
@Order
//@Component
public class queryInventoryInfoProcess extends AbstractHttpMsgProcess{
	@Autowired
	private BgManagementDaoImp bgManagementDaoImp;
	
	@Override
	public int getMessageId() {
		return SoltMessageConstants.QUERYINVENTORYINFO;
	}

	@Override
	public HttpMessageResult handler(String uuid, Map<String, String> body)
			throws Throwable {
		HttpMessageResult result = new HttpMessageResult();
		int gameId = Integer.parseInt(body.get("gameId"));
		List<SoltGameInventoryConfig> query = bgManagementDaoImp.queryInventoryInfo(gameId);
		if (query != null) {
			result.setMsg(JSONObject.toJSONString(query));
		} else {
			result.setRet(2);
			result.setMsg("queryInventoryInfo is empty!");
		}
		return result;
	}

}
