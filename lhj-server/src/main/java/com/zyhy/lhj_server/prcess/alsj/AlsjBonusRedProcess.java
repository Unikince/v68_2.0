/**
 * 
 */
package com.zyhy.lhj_server.prcess.alsj;

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
import com.zyhy.lhj_server.game.alsj.AlsjBonusInfo;
import com.zyhy.lhj_server.prcess.result.alsj.AlsjBonusRedResult;
import com.zyhy.lhj_server.service.alsj.AlsjBonusService;
import com.zyhy.lhj_server.userservice.UserService;

/**
 * @author ASUS
 *
 */
@Order
@Component
public class AlsjBonusRedProcess extends AbstractHttpMsgProcess{

	@Autowired
	private AlsjBonusService bonusService;
	
	@Autowired
	private UserService userService;
	@Autowired
	private JackPoolManager jackPoolManager;
	//@Autowired
	//private RocketmqTemplate rocketmqTemplate;
	@Override
	public int getMessageId() {
		return MessageConstants.ALSJ_BONUS_CAR;
	}

	@Override
	public HttpMessageResult handler(String uuid, Map<String, String> body)
			throws Throwable {
		String roleid = body.get("roleid");
		
		AlsjBonusRedResult result = new AlsjBonusRedResult();	
		
		// 判断是否红利游戏中
		AlsjBonusInfo hongli = bonusService.getData(roleid, uuid);
		if(hongli == null || hongli.getNum() == 0){
			result.setRet(2);
			result.setMsg("没有红利游戏");
			return result;
		}
		// 用户信息
		Player userinfo = userService.getUserInfo(roleid, uuid);
		if (userinfo == null) {
			result.setRet(2);
			result.setMsg("登录错误");
			return result;
		}
		
		// 玩家游戏币
		double usercoin = userinfo.getGold();
		// 投注前金币
		double startbalance = usercoin;
		// 回合id
		JackPool p = jackPoolManager.getPool();
		/* int randomMd5 = RandomUtil.getRandom(10000000, 99999999);
		String md5 = Md5Utils.md5(String.valueOf(randomMd5));
		int random = RandomUtil.getRandom(0, md5.length() - 6);
		String substring = md5.substring(random, random + 5);*/
		String roundId = MessageIdEnum.ALSJBY.getGameId() + "|" + userService.getOrderId();
		// 免费游戏下注
//		userService.bet(roleid, 0, roundId, uuid, Constants.BETTYPE0);
		result = bonusService.doGameRed(roleid ,hongli, userinfo, uuid);
		
		if(hongli != null && hongli.getNum() == 0){
			bonusService.deleteBonus(roleid, uuid);
		}
		// 派彩
//		userService.payout(roleid, result.getRewardAllCoin(), roundId, uuid);
		result.setUsercoin(NumberTool.add(usercoin, result.getRewardAllCoin()).doubleValue());
		// 发送日志
		int ispool = 0;
		ShowRecordResult srr = new ShowRecordResult();
		srr.setGameName(Constants.ALSJ_GAME_NAME);
		srr.setDbId("DMG17-" + roundId);
		srr.setRecordType(Constants.RECORDTYPE3);
		String date = DateUtils.format(new Date(), DateUtils.fp1);
		srr.setDate(date);
		srr.setStartbalance(startbalance);
		srr.setBet(0);
		srr.setReward(result.getRewardAllCoin());
		srr.setEndbalance(result.getUsercoin());
		srr.setGameresult(result);
		//srr.setGametime(gametime);
		srr.setRolenick(userinfo.getNickname());
		srr.setRoundId(roundId);
		GameLhjLog log = GameLhjLog.build(Constants.ALSJ_GAME_NAME, "bonus", roleid,userinfo.getNickname(),uuid,0,result.getRewardAllCoin() + 1,3,ispool, JSON.toJSONString(srr));
		//rocketmqTemplate.pushLog(Tags.LHJ_GAME, JSONObject.toJSONString(log));
		WorkManager.instance().submit(updaeInventory.class, log);
		return result;
	}

}
