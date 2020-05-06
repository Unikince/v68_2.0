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
import com.zyhy.lhj_server.bgmanagement.entity.PayoutLimit;
import com.zyhy.lhj_server.bgmanagement.entity.SoltGameInfo;

/**
 * 更新游戏信息
 */
@Order
//@Component
public class updatePayLimitInfo extends AbstractHttpMsgProcess{
	@Autowired
	private BgManagementDaoImp bgManagementDaoImp;
	
	@Override
	public int getMessageId() {
		return SoltMessageConstants.UPDATEPAYLIMITINFO;
	}

	@Override
	public HttpMessageResult handler(String uuid, Map<String, String> body)
			throws Throwable {
		HttpMessageResult result = new HttpMessageResult();
		
		PayoutLimit pl = new PayoutLimit();
		pl.setNumber(Integer.parseInt(body.get("number")));
		pl.setPayLowLimit(Double.valueOf(body.get("payLowLimit")));
		pl.setOdds(Double.valueOf(body.get("odds")));
		pl.setPayRatio(Double.valueOf(body.get("payRatio")));
		int update = bgManagementDaoImp.updatePayLimitInfo(pl);
		
		if (update <= 0) {
			result.setRet(2);
			result.setMsg("updatePayLimitInfo failed!");
		} 
		return result;
	}
}
