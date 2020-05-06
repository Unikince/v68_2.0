/**
 * 
 */
package com.zyhy.lhj_server.bgmanagement.prcess.http;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import com.zyhy.common_lhj.process.AbstractHttpMsgProcess;
import com.zyhy.common_server.result.HttpMessageResult;
import com.zyhy.lhj_server.bgmanagement.config.MessageIdEnum;
import com.zyhy.lhj_server.bgmanagement.constants.SoltMessageConstants;
import com.zyhy.lhj_server.bgmanagement.dao.imp.BgManagementDaoImp;
import com.zyhy.lhj_server.bgmanagement.entity.GamePoolConfig;

/**
 * 更新游戏信息
 */
@Order
//@Component
public class updatePoolConfigProcess extends AbstractHttpMsgProcess{
	@Autowired
	private BgManagementDaoImp bgManagementDaoImp;
	
	@Override
	public int getMessageId() {
		return SoltMessageConstants.UPDATEPOOLCONFIG;
	}

	@Override
	public HttpMessageResult handler(String uuid, Map<String, String> body)
			throws Throwable {
		HttpMessageResult result = new HttpMessageResult();
		int gameId = Integer.parseInt(body.get("gameId"));
		String poolName = body.get("poolName");
		String gameName = MessageIdEnum.getNameById(gameId);
		double initAmount = Double.parseDouble(body.get("initAmount"));
		double poolTotalRatio = Double.parseDouble(body.get("poolTotalRatio"));
		double poolOpenLow = Double.parseDouble(body.get("poolOpenLow"));
		double bonusLv = Double.parseDouble(body.get("bonusLv"));
		double rewardRatio = Double.parseDouble(body.get("rewardRatio"));
		double lowBet = Double.parseDouble(body.get("lowBet"));
		
		GamePoolConfig pc = new GamePoolConfig();
		pc.setGameId(gameId);
		pc.setGameName(gameName);
		pc.setPoolName(poolName);
		pc.setInitAmount(initAmount);
		pc.setPoolTotalRatio(poolTotalRatio);
		pc.setPoolOpenLow(poolOpenLow);
		pc.setBonusLv(bonusLv);
		pc.setRewardRatio(rewardRatio);
		pc.setLowBet(lowBet);
		int update = bgManagementDaoImp.updatePoolConfig(pc);
		if (update <= 0) {
			result.setRet(2);
			result.setMsg("updatePoolConfig failed!");
		} 
		return result;
	}

}
