/**
 * 
 */
package com.zyhy.lhj_server.bgmanagement.prcess.http;

import java.text.SimpleDateFormat;
import java.util.Date;
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
import com.zyhy.lhj_server.bgmanagement.entity.PayoutLimit;
import com.zyhy.lhj_server.bgmanagement.entity.PayoutRecord;

/**
 * 查询单个游戏信息
 */
@Order
@Component
public class queryPlayerRecordProcess extends AbstractHttpMsgProcess{
	@Autowired
	private BgManagementDaoImp bgManagementDaoImp;
	
	@Override
	public int getMessageId() {
		return SoltMessageConstants.QUERYPLAYERRECORD;
	}

	@Override
	public HttpMessageResult handler(String uuid, Map<String, String> body) {
		HttpMessageResult result = new HttpMessageResult();
		// 第几页
		int page = Integer.parseInt(body.get("page"));
		// 每页显示信息条数
		int limit = Integer.parseInt(body.get("limit"));
		//int day = Integer.parseInt(body.get("day"));
		
		List<PayoutRecord> query = bgManagementDaoImp.queryPlayerRecord(page,limit);
		int count = bgManagementDaoImp.count();
		// 查询当日和总收益
		if (query.size() > 0) {
			for (PayoutRecord pr : query) {
				PayoutRecord todayTotalReward = bgManagementDaoImp.queryTodayTotalReward(String.valueOf(pr.getPlayerId()));
				pr.setTodayWin(todayTotalReward.getTodayWin());
				pr.setTotalWin(todayTotalReward.getTotalWin());
			}
		}
		
		for (PayoutRecord payRecord : query) {
			// 查询奖励区间
			List<PayoutLimit> queryPayLimitInfo = bgManagementDaoImp.queryPayLimitInfo();
			double[] between = {0,0};
			if (queryPayLimitInfo.size() > 0) {
				for (int i = 0; i < queryPayLimitInfo.size(); i++) {
					double payLowLimit = queryPayLimitInfo.get(i).getPayLowLimit();
					if (payRecord.getReward() < payLowLimit) {
						if (i == 0) {
							between[1] = payLowLimit;
						} else {
							between[0] = queryPayLimitInfo.get(i-1).getPayLowLimit();
							between[1] = payLowLimit;
						}
					}
				}
			}
			payRecord.setRewardBetwen(between); // 设置奖励区间
			
			// 获取奖励时间
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd HH:mm");
			Date date = new Date(payRecord.getTime());
			String format = sdf.format(date);
			payRecord.setFormatTime(format);
		}
		
		if (query.size() > 0) {
			result.setRet(count);
			result.setMsg(JSONObject.toJSONString(query));
		} else {
			result.setRet(2);
			result.setMsg("queryPlayerRecord is empty!");
		}
		return result;
	}

}
