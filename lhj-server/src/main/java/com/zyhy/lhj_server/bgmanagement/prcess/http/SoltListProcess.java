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
import com.zyhy.lhj_server.bgmanagement.entity.SoltGameList;

/**
 * 获取老虎机列表
 */
@Order
//@Component
public class SoltListProcess extends AbstractHttpMsgProcess{
	@Autowired
	private BgManagementDaoImp bgManagementDaoImp;
	
	@Override
	public int getMessageId() {
		return SoltMessageConstants.SOLTLIST;
	}

	@Override
	public HttpMessageResult handler(String uuid, Map<String, String> body)
			throws Throwable {
		HttpMessageResult result = new HttpMessageResult();
		List<SoltGameList> soltList = bgManagementDaoImp.getSoltList();
		if (soltList.size() > 0) {
			result.setMsg(JSONObject.toJSONString(soltList));
		} else {
			result.setRet(2);
			result.setMsg("querysoltlist failed!");
		}
		return result;
	}

}
