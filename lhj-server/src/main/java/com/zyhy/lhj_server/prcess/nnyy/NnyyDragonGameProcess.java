/**
 * 
 */
package com.zyhy.lhj_server.prcess.nnyy;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.zyhy.common_lhj.Player;
import com.zyhy.common_lhj.pool.JackPool;
import com.zyhy.common_lhj.pool.JackPoolManager;
import com.zyhy.common_lhj.process.AbstractHttpMsgProcess;
import com.zyhy.common_server.model.GameLhjLog;
import com.zyhy.common_server.result.HttpMessageResult;
import com.zyhy.common_server.util.DateUtils;
import com.zyhy.common_server.util.NumberTool;
import com.zyhy.lhj_server.bgmanagement.config.MessageIdEnum;
import com.zyhy.lhj_server.bgmanagement.manager.WorkManager;
import com.zyhy.lhj_server.bgmanagement.prcess.work.updaeInventory;
import com.zyhy.lhj_server.constants.Constants;
import com.zyhy.lhj_server.constants.MessageConstants;
import com.zyhy.lhj_server.game.ShowRecordResult;
import com.zyhy.lhj_server.prcess.result.nnyy.NnyyDragonGameResult;
import com.zyhy.lhj_server.service.nnyy.NnyyDragonGameService;
import com.zyhy.lhj_server.userservice.UserService;

/**
 * @author ASUS
 *
 */
@Order
@Component
public class NnyyDragonGameProcess extends AbstractHttpMsgProcess{

	@Autowired
	private NnyyDragonGameService dragonGameService;
	@Autowired
	private UserService userService;
	@Autowired
	private JackPoolManager jackPoolManager;
	//@Autowired
	//private RocketmqTemplate rocketmqTemplate;
	private int count = 0;
	@Override
	public int getMessageId() {
		return MessageConstants.NNYY_POOL_GAME;
	}

	@Override
	public HttpMessageResult handler(String uuid, Map<String, String> body)
			throws Throwable {
		String roleid = body.get("roleid");
		
		// 用户信息
		Player userinfo = userService.getUserInfo(roleid, uuid);
		// 投注前金币
		double startbalance = userinfo.getGold();
		
		if(!dragonGameService.check(roleid, uuid)){
			NnyyDragonGameResult res = new NnyyDragonGameResult();
			res.setRet(2);
			res.setMsg("没有游戏资格");
			return res;
		}
		NnyyDragonGameResult res = dragonGameService.doGame(roleid, userinfo, uuid);
		// 回合id
		JackPool p = jackPoolManager.getPool();
		 /*int randomMd5 = RandomUtil.getRandom(10000000, 99999999);
		String md5 = Md5Utils.md5(String.valueOf(randomMd5));
		int random = RandomUtil.getRandom(0, md5.length() - 6);
		String substring = md5.substring(random, random + 5);
		String roundId = roleid + substring;*/
		String roundId = MessageIdEnum.NNYC.getGameId() + "|" + userService.getOrderId();
		// 免费游戏下注
//		userService.bet(roleid, 0, roundId, uuid, Constants.BETTYPE0);
		count ++;
		double payout = 0;
		if(res.getStatus() == 2 && res.getRewardcoin() > 0){
//			userService.payout(roleid, (res.getRewardcoin()), roundId, uuid);
			payout += res.getRewardcoin();
			res.setUsercoin(NumberTool.add(startbalance, res.getRewardcoin()).doubleValue());
			count = 0;
		}
		// 记录日志
		ShowRecordResult srr = new ShowRecordResult();
		int ispool = 0;
		srr.setGameName(Constants.NNYY_GAME_NAME);
		srr.setDbId("DMG17-" + roundId);
		srr.setRecordType(Constants.RECORDTYPE3);
		String date = DateUtils.format(new Date(), DateUtils.fp1);
		srr.setDate(date);
		srr.setStartbalance(startbalance);
		srr.setBet(0);
		srr.setReward(res.getRewardcoin());
		srr.setEndbalance(res.getUsercoin());
		srr.setGameresult(res);
		//srr.setGametime(gametime);
		srr.setRolenick(userinfo.getNickname());
		srr.setRoundId(roundId);
		GameLhjLog log = GameLhjLog.build(Constants.NNYY_GAME_NAME, "dragonGame", roleid,userinfo.getNickname(),uuid,0,payout,3,ispool, JSON.toJSONString(srr));
		if (res.getStatus() == 2) {
		//rocketmqTemplate.pushLog(Tags.LHJ_GAME, JSONObject.toJSONString(log));
		WorkManager.instance().submit(updaeInventory.class, log);	
		}
		return res;
	}

}
