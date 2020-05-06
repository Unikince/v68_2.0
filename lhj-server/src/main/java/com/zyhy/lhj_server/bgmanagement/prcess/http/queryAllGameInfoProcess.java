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
import com.zyhy.lhj_server.bgmanagement.entity.SoltGameInfo;

/**
 * 查询全部游戏信息
 */
@Order
//@Component
public class queryAllGameInfoProcess extends AbstractHttpMsgProcess{
	@Autowired
	private BgManagementDaoImp bgManagementDaoImp;
	
	@Override
	public int getMessageId() {
		return SoltMessageConstants.QUERYALLGAMEINFO;
	}

	@Override
	public HttpMessageResult handler(String uuid, Map<String, String> body)
			throws Throwable {
		HttpMessageResult result = new HttpMessageResult();
		List<SoltGameInfo> query = bgManagementDaoImp.queryAllGameInfo();
		if (query.size() > 0) {
			result.setMsg(JSONObject.toJSONString(query));
		} else {
			result.setRet(2);
			result.setMsg("queryAllGameInfo is empty!");
		}
		return result;
	}

}
