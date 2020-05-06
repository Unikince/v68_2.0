/**
 * 
 */
package com.zyhy.lhj_server.prcess.alsj;

import java.math.BigDecimal;
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
import com.zyhy.lhj_server.game.alsj.AlsjBonusInfo;
import com.zyhy.lhj_server.game.alsj.AlsjScatterInfo;
import com.zyhy.lhj_server.prcess.result.alsj.AlsjGameBetResult;
import com.zyhy.lhj_server.service.alsj.AlsjBonusService;
import com.zyhy.lhj_server.service.alsj.AlsjGameService;
import com.zyhy.lhj_server.userservice.UserService;

/**
 * @author linanjun 开始游戏
 */
@Order
@Component
public class AlsjStartGameInfoProcess extends AbstractHttpMsgProcess {
	@Autowired
	private BgManagementServiceImp bgManagementServiceImp;
	@Autowired
	private AlsjOpenGamePanlProcess openGamePanlProcess;
	@Autowired
	private AlsjGameService gameService;
	@Autowired
	private UserService userService;
	@Autowired
	private AlsjBonusService bonusService;
	//@Autowired
	//private RocketmqTemplate rocketmqTemplate;
	@Autowired
	private JackPoolManager jackPoolManager;
	@Override
	public int getMessageId() {
		return MessageConstants.ALSJ_START_GAME_INFO;
	}

	@Override
	public HttpMessageResult handler(String uuid, Map<String, String> body)
			throws Throwable {
		AlsjGameBetResult result = new AlsjGameBetResult();
		String roleid = body.get("roleid");
		double lineBet = Double.parseDouble(body.get("jetton"));// 线注
		int betnum = Integer.parseInt(body.get("num"));// 硬币数
		
		// 判断下注信息
		Map<Double, Integer> betList = openGamePanlProcess.betenum;
		if (betList.size() <= 0) {
			result.setRet(2);
			result.setMsg("投注信息错误");
			return result;
		}
		boolean betEnum = false;
		if (betList.containsKey(lineBet)) {
			betEnum = true;
		}
		
		if (!betEnum || betnum < 1 || betnum > 10) {
			result.setRet(2);
			result.setMsg("投注错误");
			return result;
		}
		// 赌注
		double bet = NumberTool.multiply(lineBet, 30).doubleValue();
		// 总赌注
		double totalBet = new BigDecimal(bet * betnum).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		// 用户信息
		Player userinfo = userService.getUserInfo(roleid, uuid);
		if (userinfo == null) {
			result.setRet(2);
			result.setMsg("登录错误");
			return result;
		}

		BetInfo betInfo = new BetInfo(lineBet, betnum,totalBet);
		// 判断是否红利游戏中
		AlsjBonusInfo hongli = bonusService.getData(roleid, uuid);
		if(hongli != null && hongli.getNum() > 0){
			result.setRet(2);
			result.setMsg("红利游戏中");
			return result;
		}
		// 判断是否免费游戏中
		AlsjScatterInfo bi = gameService.getData(roleid, uuid);
		boolean freeGame = false;
		if (bi != null && bi.getNum() > 0) {// 正在进行免费游戏
			betInfo = bi.getBetInfo();
			freeGame = true;
		}

		// 检查游戏币合理性
		double usercoin = userinfo.getGold();
		// 投注前金币
		double startbalance = usercoin;
		if (usercoin < totalBet && !freeGame) {
			result.setRet(4);
			result.setMsg("游戏币不足");
			return result;
		}
		// 回合id
		JackPool p = jackPoolManager.getPool();
		/* int randomMd5 = RandomUtil.getRandom(10000000, 99999999);
		String md5 = Md5Utils.md5(String.valueOf(randomMd5));
		int random = RandomUtil.getRandom(0, md5.length() - 6);
		String substring = md5.substring(random, random + 5);*/
		String roundId = MessageIdEnum.ALSJBY.getGameId() + "|" + userService.getOrderId();
		// 不是免费游戏扣除金币
		if (!freeGame) {
			// 下注
//			userService.bet(roleid, totalBet, roundId, uuid, Constants.BETTYPE1);
		} else {
			// 免费游戏扣除次数
			bi.setNum(bi.getNum() - 1);
			// 免费游戏下注
//			userService.bet(roleid, 1, roundId, uuid, Constants.BETTYPE1);
		}
		
		// 查询当前赔率
		SoltGameInfo gameInfo = bgManagementServiceImp.queryGameInfo(MessageIdEnum.ALSJBY.getRedisName());
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
				result = gameService.doGameProcess(roleid, betInfo,freeGame,iconId);
				count ++;
				if (result.getRewardcoin() >= oddsReward - oddsReward*0.1 && result.getRewardcoin() <= oddsReward
						|| (count > 200 && result.getRewardcoin() > 0)) {
					//System.out.println("本次赔率奖池的奖励为: " + result.getRewardcoin());
					break;
				}
			}
		} else {
			result = gameService.doGameProcess(roleid, betInfo,freeGame,iconId);
			// 验证大奖金额
			double checkAmount = gameInfo.getCheckAmount();
			if (result.getRewardcoin() >= checkAmount && checkAmount > 0) {
				boolean checkBigReward = bgManagementServiceImp.checkBigReward(gameInfo,result.getRewardcoin());
				if (!checkBigReward) {
					while (true) {
						result = gameService.doGameProcess(roleid, betInfo,freeGame,iconId);
						if (result.getRewardcoin() < checkAmount) {
							break;
						}
					}
				}
			}
		}
		
		//result = gameService.doGameProcess(roleid, betInfo,freeGame,1);
		
		/*// 判断通杀
		LOG.info("POOL :" + p.getCurrentnum()  + "|" +"POOL_STATUS :" + p.getStatus() 
		+ "|" + "CURRENT_REWARD :" + result.getRewardcoin());
		if ((p.getStatus() == JackPool.STATUS_KILL && result.getRewardcoin() > 0)
				|| (p.getStatus() == JackPool.STATUS_KILL && result.isBonus())
				|| (p.getStatus() == JackPool.STATUS_KILL && result.isScatter())) {
			LOG.info("STATUS_KILL_BEFORE :" + result.getRewardcoin());
			int count = 0;
			while (true) {
				result = gameService.doGameProcess(roleid, betInfo,freeGame);
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
				result = gameService.doGameProcess(roleid, betInfo,freeGame);
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
				result = gameService.doGameProcess(roleid, betInfo,freeGame);
				count ++ ;
				if (userService.checkBigReward(betInfo.getTotalBet(), result.getRewardcoin(), p.getCurrentnum()) || count == 100) {
					LOG.info("STATUS_NORMAL_AFTER:" + result.getRewardcoin() + "|" + "COUNT : " + count);
					break;
				}
			}
		}*/
		
		// 保存红利模式
		if(result.isBonus()){
			AlsjBonusInfo ab = new AlsjBonusInfo(betInfo);
			ab.setNum(5);
			ab.setFree(freeGame);
			bonusService.save(ab, roleid, userinfo, uuid);
			result.setAlsjBonusInfo(ab);
		}
		
		// 金币变化
		double change = NumberTool.subtract(result.getRewardcoin(), totalBet).doubleValue();

		// 更新免费游戏总奖励
		if (freeGame) {
			change = NumberTool.subtract(result.getRewardcoin(), 0.0).doubleValue();
			bi.setGold(NumberTool.add(bi.getGold(), result.getRewardcoin()).doubleValue());
			bi.setNum(NumberTool.add(bi.getNum(),result.getScatterNum()).intValue());
			gameService.save(bi, roleid, userinfo, uuid);
			result.setAlsjScatterInfo(bi);
			result.setScatterNum(bi.getNum());
		}

		// 是否要进行免费游戏
		if (result.isScatter() && !freeGame) {
			AlsjScatterInfo sinfo = new AlsjScatterInfo(betInfo);
			sinfo.setNum(result.getScatterNum());
			sinfo.setGold(0);
			gameService.save(sinfo, roleid, userinfo, uuid);
		}

		if(bi != null && bi.getNum() == 0){
			gameService.deleteInfo(roleid, uuid);
		}
		
		// 派彩
		double payout = 0;
//		userService.payout(roleid, result.getRewardcoin(), roundId, uuid);
		payout = result.getRewardcoin();
		result.setUsercoin(NumberTool.add(usercoin, change).doubleValue());
		
		// 记录日志
		//gameService.saveBigReward(Constants.ALSJ_SERVER_NAME, betInfo, result.getRewardcoin(),roleid);
		int ispool = 0;
		if (oddsReward > 0) {
			ispool = 1;
		}
		int type = 0;
		if (freeGame) {
			type = 2;
		}else {
			type = 1;
		}
		ShowRecordResult srr = new ShowRecordResult();
		srr.setGameName(Constants.ALSJ_GAME_NAME);
		srr.setDbId("DMG17-" + roundId);
		if (freeGame) {
			srr.setRecordType(Constants.RECORDTYPE2);
		}else {
			srr.setRecordType(Constants.RECORDTYPE1);
		}
		String date = DateUtils.format(new Date(), DateUtils.fp1);
		srr.setDate(date);
		srr.setStartbalance(startbalance);
		double bet1 = 0;
		if (freeGame) {
			bet1 = 0;
		}else {
			bet1 = totalBet;
		}
		srr.setBet(bet1);
		srr.setReward(result.getRewardcoin());
		srr.setEndbalance(result.getUsercoin());
		srr.setGameresult(result);
		//srr.setGametime(gametime);
		srr.setRolenick(userinfo.getNickname());
		srr.setRoundId(roundId);
		//String gamename, String logtype, String roleid, String rolenick,String uuid, double bet, double reward,int rewardtype, int ispool,String fujia
		GameLhjLog log = GameLhjLog.build(Constants.ALSJ_GAME_NAME, "start",roleid,userinfo.getNickname(),uuid,bet1,payout,type,ispool,JSON.toJSONString(srr));
		//rocketmqTemplate.pushLog(Tags.LHJ_GAME, JSONObject.toJSONString(log));
		WorkManager.instance().submit(updaeInventory.class, log);
		return result;
	}

}
