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
import com.zyhy.lhj_server.bgmanagement.entity.SoltGameInfo;

/**
 * 更新游戏信息
 */
@Order
//@Component
public class updateSoltInfoProcess extends AbstractHttpMsgProcess{
	@Autowired
	private BgManagementDaoImp bgManagementDaoImp;
	
	@Override
	public int getMessageId() {
		return SoltMessageConstants.UPDATESLOTINFO;
	}

	@Override
	public HttpMessageResult handler(String uuid, Map<String, String> body)
			throws Throwable {
		HttpMessageResult result = new HttpMessageResult();
		int gameId = Integer.parseInt(body.get("gameId"));
		int number = Integer.parseInt(body.get("number"));
		String betList = body.get("betList");
		boolean checkBetList = checkBetList(betList);
		if (!checkBetList) {
			result.setRet(2);
			result.setMsg("betList is erro!");
			return result;
		}
		/*SoltGameInfo checkNumber = bgManagementDaoImp.checkNumber(gameId, number);
		if (checkNumber != null) {
			result.setRet(2);
			result.setMsg("number exists!");
			return result;
		}*/
		SoltGameInfo data = new SoltGameInfo();
		data.setGameId(gameId);
		data.setNumber(number);
		data.setState(Integer.parseInt(body.get("state")));
		data.setTotalPlayerNumber(Integer.parseInt(body.get("totalPlayerNumber")));
		data.setWinReward(Integer.parseInt(body.get("winReward")));
		data.setBetList(betList);
		data.setInAmount(Integer.parseInt(body.get("inAmount")));
		data.setCheckAmount(Double.valueOf(body.get("checkAmount")));
		data.setCheckOdds(Double.valueOf(body.get("checkOdds")));
		int update = bgManagementDaoImp.updateSoltInfo(data);
		if (update <= 0) {
			result.setRet(2);
			result.setMsg("updateSoltInfo failed!");
		} 
		return result;
	}
private boolean checkBetList(String betList){
	if (betList.startsWith("[")) {
		String sub1 = betList.substring(1, betList.length());
		if (sub1.endsWith("]")) {
			String sub2 = sub1.substring(0, sub1.length() - 1);
			String[] split = sub2.split("\\,");
			for (String string : split) {
				double bet = 0;
				try {
					bet = Double.valueOf(string);
				} catch (Exception e) {
					return false;
				}
				if (bet <= 0) {
					return false;
				}
			}
			return true;
		}
	} 
	return false;
}
}
