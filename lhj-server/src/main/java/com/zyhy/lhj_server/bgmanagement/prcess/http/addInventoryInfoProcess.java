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
import com.zyhy.lhj_server.bgmanagement.entity.SoltGameInventoryConfig;

/**
 * 添加库存配置
 */
@Order
//@Component
public class addInventoryInfoProcess extends AbstractHttpMsgProcess{
	@Autowired
	private BgManagementDaoImp bgManagementDaoImp;
	
	@Override
	public int getMessageId() {
		return SoltMessageConstants.ADDINVENTORYINFO;
	}

	@Override
	public HttpMessageResult handler(String uuid, Map<String, String> body)
			throws Throwable {
		HttpMessageResult result = new HttpMessageResult();
		
		int gameId = Integer.parseInt(body.get("gameId"));
		String gamename = MessageIdEnum.getNameById(gameId);
		int number = Integer.parseInt(body.get("number"));
		
		SoltGameInventoryConfig data = new SoltGameInventoryConfig();
		data.setGameId(gameId);
		data.setGamename(gamename);
		data.setNumber(number);
		data.setSvalue(Double.parseDouble(body.get("svalue")));
		data.setBvalue(Double.parseDouble(body.get("bvalue")));
		data.setOdds(Double.parseDouble(body.get("odds")));
		
		SoltGameInventoryConfig inventoryInfo = bgManagementDaoImp.queryInventoryInfo(gameId, number);
		if (inventoryInfo != null) {
			result.setRet(2);
			result.setMsg("number exists!");
			return result;
		}
		
		int update = bgManagementDaoImp.addInventoryInfo(data);
		
		if (update != 1) {
			result.setRet(2);
			result.setMsg("addInventoryInfo failed!");
		}
		return result;
	}

}
