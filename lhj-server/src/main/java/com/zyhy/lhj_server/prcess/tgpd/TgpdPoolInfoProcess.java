/**
 * 
 */
package com.zyhy.lhj_server.prcess.tgpd;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.zyhy.common_lhj.pool.DragonPool;
import com.zyhy.common_lhj.process.AbstractHttpMsgProcess;
import com.zyhy.common_server.result.HttpMessageResult;
import com.zyhy.lhj_server.constants.Constants;
import com.zyhy.lhj_server.constants.MessageConstants;
import com.zyhy.lhj_server.game.tgpd.TgpdDragonPool;
import com.zyhy.lhj_server.game.tgpd.TgpdPoolManager;
import com.zyhy.lhj_server.prcess.result.tgpd.TgpdPoolResult;
import com.zyhy.lhj_server.service.tgpd.TgpdGameService;

/**
 * @author ASUS
 *
 */
@Order
@Component
public class TgpdPoolInfoProcess extends AbstractHttpMsgProcess{
	@Autowired
	private TgpdPoolManager poolManager;
	@Autowired
	private TgpdGameService gameService;
	@Override
	public int getMessageId() {
		return MessageConstants.TGPD_POOL_INFO;
	}

	@Override
	public HttpMessageResult handler(String uuid, Map<String, String> body)
			throws Throwable {
		String roleid = body.get("roleid");
		int type = Integer.parseInt(body.get("type"));
		DragonPool pool = poolManager.getPool();
		TgpdPoolResult res = new TgpdPoolResult();
		res.setGrand(new BigDecimal(pool.getGrand()).setScale(2, BigDecimal.ROUND_DOWN).doubleValue());
		res.setMajor(new BigDecimal(pool.getMajor()).setScale(2, BigDecimal.ROUND_DOWN).doubleValue());
		res.setMinor(new BigDecimal(pool.getMinor()).setScale(2, BigDecimal.ROUND_DOWN).doubleValue());
		res.setMini(new BigDecimal(pool.getMini()).setScale(2, BigDecimal.ROUND_DOWN).doubleValue());
		
		// 保存心跳数据
		if (type == 1) {
			gameService.saveHeartInfo(roleid, uuid);
		}
		// 玩家正常退出,删除游戏数据
		if(type == 2){
			gameService.delFreeInfoCache(roleid, Constants.REDIS_LHJ_TGPD_GAMEINFO, uuid);
			gameService.delFreeInfoCache(roleid, Constants.REDIS_LHJ_TGPD_HEART_DATA, uuid);
			gameService.delFreeInfoCache(roleid, Constants.REDIS_LHJ_TGPD_REPLENISH, uuid);
			gameService.delFreeInfoCache(roleid, Constants.REDIS_LHJ_TGPD_FREE, uuid);
		}
		return res;
	}

}
