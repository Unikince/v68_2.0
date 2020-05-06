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
import com.zyhy.lhj_server.bgmanagement.entity.PayoutPlayerLimit;

/**
 * 添加老虎机游戏
 */
@Order
//@Component
public class addPayPlayerLimitInfoProcess extends AbstractHttpMsgProcess{
	@Autowired
	private BgManagementDaoImp bgManagementDaoImp;
	
	@Override
	public int getMessageId() {
		return SoltMessageConstants.ADDPAYPLAYERLIMITINFO;
	}

	@Override
	public HttpMessageResult handler(String uuid, Map<String, String> body)
			throws Throwable {
		HttpMessageResult result = new HttpMessageResult();
		PayoutPlayerLimit ppl = new PayoutPlayerLimit();
		ppl.setNumber(Integer.parseInt(body.get("number")));
		ppl.setTotalLowLimit(Double.valueOf(body.get("totalLowLimit")));
		ppl.setDayLowLimit(Double.valueOf(body.get("dayLowLimit")));
		ppl.setTotalWaterLow(Double.valueOf(body.get("totalWaterLow")));
		ppl.setDayWaterLow(Double.valueOf(body.get("dayWaterLow")));
		ppl.setOddsTotalHight(Double.valueOf(body.get("oddsTotalHight")));
		ppl.setDayOddsHight(Double.valueOf(body.get("dayOddsHight")));
		int update = bgManagementDaoImp.addPayPlayerLimitInfo(ppl);
		if (update != 1) {
			result.setRet(2);
			result.setMsg("addPayPlayerLimitInfo failed!");
		}
		return result;
	}

}
