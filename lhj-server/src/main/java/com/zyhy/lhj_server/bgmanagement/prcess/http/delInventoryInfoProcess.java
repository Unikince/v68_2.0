/**
 * 
 */
package com.zyhy.lhj_server.bgmanagement.prcess.http;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.zyhy.common_lhj.process.AbstractHttpMsgProcess;
import com.zyhy.common_server.result.HttpMessageResult;
import com.zyhy.lhj_server.bgmanagement.constants.SoltMessageConstants;
import com.zyhy.lhj_server.bgmanagement.dao.imp.BgManagementDaoImp;

/**
 * 删除配置信息
 */
@Order
//@Component
public class delInventoryInfoProcess extends AbstractHttpMsgProcess{
	@Autowired
	private BgManagementDaoImp bgManagementDaoImp;
	
	@Override
	public int getMessageId() {
		return SoltMessageConstants.DELINVENTORYINFO;
	}

	@Override
	public HttpMessageResult handler(String uuid, Map<String, String> body)
			throws Throwable {
		HttpMessageResult result = new HttpMessageResult();
		int gameId = Integer.parseInt(body.get("gameId"));
		int number = Integer.parseInt(body.get("number"));
		int update = bgManagementDaoImp.delInventoryInfo(gameId, number);
		if (update != 1) {
			result.setRet(2);
			result.setMsg("delInventoryInfo failed!");
		}
		return result;
	}

}
