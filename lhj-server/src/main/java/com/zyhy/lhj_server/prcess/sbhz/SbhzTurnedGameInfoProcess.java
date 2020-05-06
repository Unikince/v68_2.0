/**
 * 
 */
package com.zyhy.lhj_server.prcess.sbhz;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.zyhy.common_lhj.BetInfo;
import com.zyhy.common_lhj.Player;
import com.zyhy.common_lhj.pool.JackPoolManager;
import com.zyhy.common_lhj.process.AbstractHttpMsgProcess;
import com.zyhy.common_server.model.GameLhjLog;
import com.zyhy.common_server.result.HttpMessageResult;
import com.zyhy.common_server.util.DateUtils;
import com.zyhy.common_server.util.NumberTool;
import com.zyhy.lhj_server.bgmanagement.config.MessageIdEnum;
import com.zyhy.lhj_server.bgmanagement.entity.SoltGameInfo;
import com.zyhy.lhj_server.bgmanagement.manager.WorkManager;
import com.zyhy.lhj_server.bgmanagement.prcess.work.updaeInventory;
import com.zyhy.lhj_server.bgmanagement.service.imp.BgManagementServiceImp;
import com.zyhy.lhj_server.constants.Constants;
import com.zyhy.lhj_server.constants.MessageConstants;
import com.zyhy.lhj_server.game.GameOddsEnum;
import com.zyhy.lhj_server.game.ShowRecordResult;
import com.zyhy.lhj_server.game.sbhz.SbhzWildMonKey;
import com.zyhy.lhj_server.prcess.result.sbhz.SbhzGameBetResult;
import com.zyhy.lhj_server.service.sbhz.SbhzGameService;
import com.zyhy.lhj_server.userservice.UserService;

/**
 * @author linanjun
 * 开始游戏
 */
@Order
@Component
public class SbhzTurnedGameInfoProcess extends AbstractHttpMsgProcess{
	@Autowired
	private BgManagementServiceImp bgManagementServiceImp;
	@Autowired
	private SbhzGameService gameService;
	@Autowired
	private UserService userService;
	//@Autowired
	//private RocketmqTemplate rocketmqTemplate;
	@Autowired
	private JackPoolManager jackPoolManager;
	private static final Logger LOG = LoggerFactory.getLogger(Constants.SBHZ_SERVER_NAME);
	@Override
	public int getMessageId() {
		return MessageConstants.SBHZ_START_GAME_TURNED;
	}

	@Override
	public HttpMessageResult handler(String uuid, Map<String, String> body)
			throws Throwable {
		// 返回信息
		SbhzGameBetResult result = new SbhzGameBetResult();
		String roleid = body.get("roleid");
		// 用户信息
		Player userinfo = userService.getUserInfo(roleid, uuid);
		if(userinfo == null){
			result.setRet(2);
			result.setMsg("登录错误");
			return result;
		}
		//总投注额度
		double m = 0;
		// 玩家游戏币
		double usercoin = userinfo.getGold();
		// 投注前金币
		double startbalance = usercoin;
		
		//判断是否重转中
		SbhzWildMonKey mk = gameService.getData(roleid, uuid);
		if(mk != null && !mk.isMonKey() || mk == null){
			result.setRet(2);
			result.setMsg("没有重转数据");
			return result;
		}
		BetInfo betInfo = mk.getBetInfo();
		
		
		// 查询当前赔率
		SoltGameInfo gameInfo = bgManagementServiceImp.queryGameInfo(MessageIdEnum.SBHZ.getRedisName());
		double odds = bgManagementServiceImp.queryLhjCurrentOdds(gameInfo);
		//System.out.println("gameInfo" + gameInfo);
		//System.out.println("当前游戏的赔率: " + gameInfo.getOdds());
		int iconId = GameOddsEnum.getIdByOdds(odds);
		//System.out.println("使用第几套图标" + iconId);
		if (iconId == 0) {
			iconId = 4;
			//System.out.println("没有查询到赔率,使用第"+  iconId +"套图标" );
		}
		
		result = gameService.doGameProcess(roleid, betInfo,mk,iconId);
		// 验证大奖金额
		double checkAmount = gameInfo.getCheckAmount();
		if (result.getRewardcoin() >= checkAmount && checkAmount > 0) {
			boolean checkBigReward = bgManagementServiceImp.checkBigReward(gameInfo,result.getRewardcoin());
			if (!checkBigReward) {
				while (true) {
					result = gameService.doGameProcess(roleid, betInfo,mk,iconId);
					if (result.getRewardcoin() < checkAmount) {
						break;
					}
				}
			}
		}
		
		//result = gameService.doGameProcess(roleid, betInfo,mk,1);
		
		
		// 判断通杀
		/*LOG.info("POOL :" + p.getCurrentnum()  + "|" +"POOL_STATUS :" + p.getStatus() 
		+ "|" + "CURRENT_REWARD :" + result.getRewardcoin());
		if (p.getStatus() == JackPool.STATUS_KILL && result.getRewardcoin() > 0) {
			LOG.info("STATUS_KILL_BEFORE :" + result.getRewardcoin());
			int count = 0;
			while (true) {
				result = gameService.doGameProcess(roleid, betInfo,mk);
				count ++ ;
				if (result.getRewardcoin() == 0 || count == 100) {
					LOG.info("STATUS_KILL_AFTER :" + result.getRewardcoin() + "|" + "COUNT : " + count);
					break;
				}
			}
		}*/
		// 判断放水
		/*if (p.getStatus() == JackPool.STATUS_WIN && result.getRewardcoin() == 0) {
			LOG.info("STATUS_WIN_BEFORE :" + result.getRewardcoin());
			int count = 0;
			while (true) {
				result = gameService.doGameProcess(roleid, betInfo,mk);
				count ++ ;
				if (result.getRewardcoin() > 0 || count == 100) {
					LOG.info("STATUS_WIN_AFTER:" + result.getRewardcoin() + "|" + "COUNT : " + count);
					break;
				}
			}
		}*/
		
		/*// 正常模式
		if (result.getRewardcoin() > 0 && 
				!userService.checkBigReward(betInfo.getTotalBet(), result.getRewardcoin(), p.getCurrentnum())) {
			LOG.info("STATUS_NORMAL_BEFORE :" + result.getRewardcoin());
			int count = 0;
			while (true) {
				result = gameService.doGameProcess(roleid, betInfo,mk);
				count ++ ;
				if (userService.checkBigReward(betInfo.getTotalBet(), result.getRewardcoin(), p.getCurrentnum()) || count == 100) {
					LOG.info("STATUS_NORMAL_AFTER:" + result.getRewardcoin() + "|" + "COUNT : " + count);
					break;
				}
			}
		}*/
		
		// 重转信息
		if(result.isTurned()){
			gameService.save(result.getMonkey(), roleid, uuid);
		} else {
			gameService.delete(roleid, uuid);
		}
		
		//金币变化
		double change = NumberTool.subtract(result.getRewardcoin(), m).doubleValue();
		// 回合id
		/* int randomMd5 = RandomUtil.getRandom(10000000, 99999999);
		String md5 = Md5Utils.md5(String.valueOf(randomMd5));
		int random = RandomUtil.getRandom(0, md5.length() - 6);
		String substring = md5.substring(random, random + 5);
		String roundId = roleid + substring;*/
		String roundId = MessageIdEnum.SBHZ.getGameId() + "|" + userService.getOrderId();
		// 免费游戏下注
//		userService.bet(roleid, 0, roundId, uuid, Constants.BETTYPE0);
		// 派彩
//		userService.payout(roleid, result.getRewardcoin(), roundId, uuid);
		result.setUsercoin(NumberTool.add(usercoin, change).doubleValue());
		// 记录日志
		//gameService.saveBigReward(Constants.SBHZ_SERVER_NAME, betInfo, result.getRewardcoin(),roleid);
		ShowRecordResult srr = new ShowRecordResult();
		int ispool = 0;
		srr.setGameName(Constants.SBHZ_GAME_NAME);
		srr.setDbId("DMG17-" + roundId);
		srr.setRecordType(Constants.RECORDTYPE2);
		String date = DateUtils.format(new Date(), DateUtils.fp1);
		srr.setDate(date);
		srr.setStartbalance(startbalance);
		srr.setBet(0);
		srr.setReward(result.getRewardcoin());
		srr.setEndbalance(result.getUsercoin());
		srr.setGameresult(result);
		//srr.setGametime(gametime);
		srr.setRolenick(userinfo.getNickname());
		srr.setRoundId(roundId);
		GameLhjLog log = GameLhjLog.build(Constants.SBHZ_GAME_NAME, "Turned", roleid,userinfo.getNickname(),uuid,0,result.getRewardcoin() + 1,2,ispool, JSON.toJSONString(srr));
		//rocketmqTemplate.pushLog(Tags.LHJ_GAME, JSONObject.toJSONString(log));
		WorkManager.instance().submit(updaeInventory.class, log);
		return result;
	}

}
