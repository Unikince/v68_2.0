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
import com.zyhy.lhj_server.bgmanagement.entity.OddsPoolConfig;

/**
 * 查询赔率奖池信息
 */
@Order
//@Component
public class queryOddsPoolInfoProcess extends AbstractHttpMsgProcess{
	@Autowired
	private BgManagementDaoImp bgManagementDaoImp;
	
	@Override
	public int getMessageId() {
		return SoltMessageConstants.QUERYODDSPOOLINFO;
	}

	@Override
	public HttpMessageResult handler(String uuid, Map<String, String> body)
			throws Throwable {
		HttpMessageResult result = new HttpMessageResult();
		
		OddsPoolConfig query = bgManagementDaoImp.queryOddsPoolInfo();
		if (query != null) {
			result.setMsg(JSONObject.toJSONString(query));
		} else {
			result.setRet(2);
			result.setMsg("queryOddsPoolInfo is empty!");
		}
		return result;
	}

}
