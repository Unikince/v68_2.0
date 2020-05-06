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
import com.zyhy.lhj_server.bgmanagement.entity.SoltGameInfo;

/**
 * 添加老虎机游戏
 */
@Order
//@Component
public class addSoltInfoProcess extends AbstractHttpMsgProcess{
	@Autowired
	private BgManagementDaoImp bgManagementDaoImp;
	
	@Override
	public int getMessageId() {
		return SoltMessageConstants.ADDSOLTINFO;
	}

	@Override
	public HttpMessageResult handler(String uuid, Map<String, String> body)
			throws Throwable {
		HttpMessageResult result = new HttpMessageResult();
		// 游戏id
		String gamename = body.get("gamename");
		int gameId = MessageIdEnum.getIdByName(gamename);
		// 游戏序号
		int number =  Integer.parseInt(body.get("number"));
		SoltGameInfo checkNumber = bgManagementDaoImp.checkNumber(gameId, number);
		if (checkNumber != null) {
			result.setRet(2);
			result.setMsg("number exists!");
			return result;
		}
		int update = bgManagementDaoImp.addSoltInfo(gamename, number);
		if (update != 1) {
			result.setRet(2);
			result.setMsg("addgame failed!");
		}
		return result;
	}

}
