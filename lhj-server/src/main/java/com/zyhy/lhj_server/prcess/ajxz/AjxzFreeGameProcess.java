/**
 * 
 */
package com.zyhy.lhj_server.prcess.ajxz;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.zyhy.common_lhj.BetInfo;
import com.zyhy.common_lhj.Player;
import com.zyhy.common_lhj.pool.JackPool;
import com.zyhy.common_lhj.pool.JackPoolManager;
import com.zyhy.common_lhj.process.AbstractHttpMsgProcess;
import com.zyhy.common_server.model.GameLhjLog;
import com.zyhy.common_server.result.HttpMessageResult;
import com.zyhy.common_server.util.DateUtils;
import com.zyhy.common_server.util.NumberTool;
import com.zyhy.common_server.util.RandomUtil;
import com.zyhy.lhj_server.bgmanagement.config.MessageIdEnum;
import com.zyhy.lhj_server.bgmanagement.entity.PayoutLimit;
import com.zyhy.lhj_server.bgmanagement.entity.SoltGameInfo;
import com.zyhy.lhj_server.bgmanagement.manager.WorkManager;
import com.zyhy.lhj_server.bgmanagement.prcess.work.updaeInventory;
import com.zyhy.lhj_server.bgmanagement.service.imp.BgManagementServiceImp;
import com.zyhy.lhj_server.constants.Constants;
import com.zyhy.lhj_server.constants.MessageConstants;
import com.zyhy.lhj_server.game.GameOddsEnum;
import com.zyhy.lhj_server.game.ShowRecordResult;
import com.zyhy.lhj_server.game.ajxz.AjxzBetEnum;
import com.zyhy.lhj_server.game.ajxz.AjxzFreeInfo;
import com.zyhy.lhj_server.game.ajxz.AjxzWinLineEnum;
import com.zyhy.lhj_server.prcess.result.ajxz.AjxzGameBetResult;
import com.zyhy.lhj_server.service.ajxz.AjxzFreeGameService;
import com.zyhy.lhj_server.service.ajxz.AjxzGameService;
import com.zyhy.lhj_server.userservice.UserService;

/**
 * 开始游戏
 */
@Order
@Component
public class AjxzFreeGameProcess extends AbstractHttpMsgProcess{
	@Autowired
	private BgManagementServiceImp bgManagementServiceImp;
	@Autowired
	private AjxzGameService gameService;
	@Autowired
	private AjxzFreeGameService freeGameService;
	@Autowired
	private UserService userService;
	//@Autowired
	//private RocketmqTemplate rocketmqTemplate;
	@Autowired
	private JackPoolManager jackPoolManager;
	@Override
	public int getMessageId() {
		return MessageConstants.AJXZ_FREE_GAME;
	}

	@Override
	public HttpMessageResult handler(String uuid, Map<String, String> body)
			throws Throwable {
		// 返回信息
		AjxzGameBetResult result = new AjxzGameBetResult();
		String roleid = body.get("roleid");
		double lineBet = Double.parseDouble(body.get("jetton"));//档位
		int betnum = Integer.parseInt(body.get("num"));//级别 1-9
		int type = Integer.parseInt(body.get("type"));
		AjxzBetEnum betEnum = AjxzBetEnum.getByBetcoin(lineBet);
		if(betEnum == null || betnum < 1 || betnum > AjxzWinLineEnum.values().length){
			result.setRet(2);
			result.setMsg("投注错误");
			return result;
		}
		// 用户信息
		Player userinfo = userService.getUserInfo(roleid, uuid);
		if(userinfo == null){
			result.setRet(2);
			result.setMsg("登录错误");
			return result;
		}
		// 玩家游戏币
		double usercoin = userinfo.getGold();
		double startbalance = usercoin;
		//总额
		AjxzFreeInfo freeInfo = gameService.getFreeInfo(roleid, uuid);
		BetInfo betInfo = freeInfo.getBetInfo();
		
		// 记录免费游戏类型
		 if (type == 5 ||type == 10||type == 15) {
			 freeInfo.setType(type);
		}
		
		// 回合id
		 JackPool p = jackPoolManager.getPool();
		/*int randomMd5 = RandomUtil.getRandom(10000000, 99999999);
		String md5 = Md5Utils.md5(String.valueOf(randomMd5));
		int random = RandomUtil.getRandom(0, md5.length() - 6);
		String substring = md5.substring(random, random + 5);*/
		 String roundId = MessageIdEnum.AJXZ.getGameId() + "|" + userService.getOrderId();
		// 免费游戏下注
		//userService.bet(roleid, 0, roundId, uuid, Constants.BETTYPE0);
		 
		// 查询当前赔率
		SoltGameInfo gameInfo = bgManagementServiceImp.queryGameInfo(MessageIdEnum.AJXZ.getRedisName());
		double odds = bgManagementServiceImp.queryLhjCurrentOdds(gameInfo);
		//System.out.println("gameInfo" + gameInfo);
		//System.out.println("当前游戏的赔率: " + gameInfo.getOdds());
		int iconId = GameOddsEnum.getIdByOdds(odds);
		//System.out.println("使用第几套图标" + iconId);
		if (iconId == 0) {
			iconId = 4;
			//System.out.println("没有查询到赔率,使用第"+  iconId +"套图标" );
		}
		// 查询是否中奖池游戏
		PayoutLimit queryWinlimit = bgManagementServiceImp.queryWinlimit();
		//System.out.println("当前派奖条件" + queryWinlimit);
		// 是否中赔率奖池奖励
		boolean isOddsPool = false;
		// 中奖金额
		double oddsReward = 0;
		if (queryWinlimit != null) {
			// 查询玩家是否满足赔率奖池中奖条件
			isOddsPool = bgManagementServiceImp.queryPlayerWinlimit(roleid);
			//System.out.println("是否中赔率奖池奖励" + isOddsPool);
		}
		
		if (isOddsPool) {
			// 中奖概率
			double odds2 = queryWinlimit.getOdds() * 10000;
			//System.out.println("当前赔率奖池奖励的中奖率" + odds2);
			double random = RandomUtil.getRandom(1.0, 10000.0);
			if (random > 10000.0 - odds2) {
				oddsReward = queryWinlimit.getPayLowLimit() * queryWinlimit.getPayRatio();
				iconId = 8;
			}
		}
		//System.out.println("赔率奖池的奖励为为=========>" + oddsReward);
		//System.out.println("当前使用第几套图标: " + iconId);
		// 执行游戏逻辑
		if (oddsReward > 0) {
			int count = 0;
			while (true) {
				result = freeGameService.doGameProcess(roleid, betInfo , freeInfo, iconId);
				count ++;
				if (result.getRewardcoin() >= oddsReward - oddsReward*0.1 && result.getRewardcoin() <= oddsReward
						|| (count > 200 && result.getRewardcoin() > 0)) {
					//System.out.println("本次赔率奖池的奖励为: " + result.getRewardcoin());
					break;
				}
			}
		} else {
			result = freeGameService.doGameProcess(roleid, betInfo , freeInfo, iconId);
			// 验证大奖金额
			double checkAmount = gameInfo.getCheckAmount();
			if (result.getRewardcoin() >= checkAmount && checkAmount > 0) {
				boolean checkBigReward = bgManagementServiceImp.checkBigReward(gameInfo,result.getRewardcoin());
				if (!checkBigReward) {
					while (true) {
							result = freeGameService.doGameProcess(roleid, betInfo , freeInfo, iconId);
							break;
					}
				}
			}
		}
		
		//result = freeGameService.doGameProcess(roleid, betInfo , freeInfo, 1);
		
		
		/*// 判断通杀
		LOG.info("POOL :" + p.getCurrentnum()  + "|" +"POOL_STATUS :" + p.getStatus() 
		+ "|" + "CURRENT_REWARD :" + result.getRewardcoin());
		if ((p.getStatus() == JackPool.STATUS_KILL && result.getRewardcoin() > 0)
				|| (p.getStatus() == JackPool.STATUS_KILL && result.isFree())) {
			LOG.info("STATUS_KILL_BEFORE :" + result.getRewardcoin());
			int count = 0;
			while (true) {
				result = freeGameService.doGameProcess(roleid, betInfo , freeInfo);
				count ++ ;
				if (result.getRewardcoin() == 0 || count == 100) {
					LOG.info("STATUS_KILL_AFTER :" + result.getRewardcoin() + "|" + "COUNT : " + count);
					break;
				}
			}
		}
		// 判断放水
		if (p.getStatus() == JackPool.STATUS_WIN && result.getRewardcoin() == 0) {
			LOG.info("STATUS_WIN_BEFORE :" + result.getRewardcoin());
			int count = 0;
			while (true) {
				result = freeGameService.doGameProcess(roleid, betInfo , freeInfo);
				count ++ ;
				if ((result.getRewardcoin() > 0  
						&& userService.checkBigReward(betInfo.getTotalBet(), result.getRewardcoin(), p.getCurrentnum()))|| count == 100) {
					LOG.info("STATUS_WIN_AFTER:" + result.getRewardcoin() + "|" + "COUNT : " + count);
					break;
				}
			}
		}
		// 正常模式
		if (result.getRewardcoin() > 0 && 
				!userService.checkBigReward(betInfo.getTotalBet(), result.getRewardcoin(), p.getCurrentnum())) {
			LOG.info("STATUS_NORMAL_BEFORE :" + result.getRewardcoin());
			int count = 0;
			while (true) {
				result = freeGameService.doGameProcess(roleid, betInfo , freeInfo);
				count ++ ;
				if (userService.checkBigReward(betInfo.getTotalBet(), result.getRewardcoin(), p.getCurrentnum()) || count == 100) {
					LOG.info("STATUS_NORMAL_AFTER:" + result.getRewardcoin() + "|" + "COUNT : " + count);
					break;
				}
			}
		}*/
		
		// 计算免费游戏的总奖励
		freeInfo.setGold(freeInfo.getGold() + result.getRewardcoin());
		
		// 更新免费游戏信息
		if (freeInfo.getType() == 5) {
			if (result.isFree()) {
				freeInfo.setFreeNum_5(freeInfo.getFreeNum_5() + 5);			
			}
			freeInfo.setFreeNum_5(freeInfo.getFreeNum_5() - 1);
			result.setFreeNum(freeInfo.getFreeNum_5());
			gameService.saveFreeInfoCache(roleid, freeInfo, userinfo, uuid);
			if(freeInfo.getFreeNum_5() == 0){
				gameService.delFreeInfoCache(roleid, uuid);
			}
		}
		
		if (freeInfo.getType() == 10) {
			if (result.isFree()) {
				freeInfo.setFreeNum_10(freeInfo.getFreeNum_10() + 10);			
			}
			freeInfo.setFreeNum_10(freeInfo.getFreeNum_10() - 1);
			result.setFreeNum(freeInfo.getFreeNum_10());
			gameService.saveFreeInfoCache(roleid, freeInfo, userinfo, uuid);
			if(freeInfo.getFreeNum_10() == 0){
				gameService.delFreeInfoCache(roleid, uuid);
			}
		}
		if (freeInfo.getType() == 15) {
			if (result.isFree()) {
				freeInfo.setFreeNum_15(freeInfo.getFreeNum_15() + 15);			
			}
			freeInfo.setFreeNum_15(freeInfo.getFreeNum_15() - 1);
			result.setFreeNum(freeInfo.getFreeNum_15());
			gameService.saveFreeInfoCache(roleid, freeInfo, userinfo, uuid);
			if(freeInfo.getFreeNum_15() == 0){
				gameService.delFreeInfoCache(roleid, uuid);
			}
		}
		result.setFreeInfo(freeInfo);
		
		//金币变化
		double change = NumberTool.subtract(result.getRewardcoin(), 0.0).doubleValue();
		
		// 派彩
		//userService.payout(roleid, result.getRewardcoin(), roundId, uuid);
		result.setUsercoin(NumberTool.add(usercoin, change).doubleValue());
		
		// 保存大奖
		//gameService.saveBigReward(Constants.AJXZ_SERVER_NAME, betInfo, result.getRewardcoin(),roleid);
		// 保存日志
		int ispool = 0;
		if (oddsReward > 0) {
			ispool = 1;
		}
		ShowRecordResult srr = new ShowRecordResult();
		srr.setGameName(Constants.AJXZ_GAME_NAME);
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
		//String gamename, String logtype, String roleid, String rolenick,String uuid, double bet, double reward,int rewardtype, int ispool,String fujia
		GameLhjLog log = GameLhjLog.build(Constants.AJXZ_GAME_NAME, "bonus", roleid,userinfo.getNickname(),uuid,0,result.getRewardcoin() + 1,2,ispool, JSON.toJSONString(srr));
		//rocketmqTemplate.pushLog(Tags.LHJ_GAME, JSONObject.toJSONString(log));
		WorkManager.instance().submit(updaeInventory.class, log);
		return result;
	}

}
