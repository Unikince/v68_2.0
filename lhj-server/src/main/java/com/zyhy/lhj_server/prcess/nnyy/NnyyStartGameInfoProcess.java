/**
 * 
 */
package com.zyhy.lhj_server.prcess.nnyy;

import java.math.BigDecimal;
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
import com.zyhy.lhj_server.game.nnyy.NnyyWinLineEnum;
import com.zyhy.lhj_server.manager.pool.PoolManager;
import com.zyhy.lhj_server.prcess.result.nnyy.NnyyGameBetResult;
import com.zyhy.lhj_server.service.nnyy.NnyyDragonGameService;
import com.zyhy.lhj_server.service.nnyy.NnyyGameService;
import com.zyhy.lhj_server.userservice.UserService;

/**
 * @author linanjun
 * 开始游戏
 */
@Order
@Component
public class NnyyStartGameInfoProcess extends AbstractHttpMsgProcess{
	@Autowired
	private BgManagementServiceImp bgManagementServiceImp;
	@Autowired
	private NnyyOpenGamePanlProcess openGamePanlProcess;
	@Autowired
	private NnyyGameService gameService;
	@Autowired
	private UserService userService;
	//@Autowired
	//private RocketmqTemplate rocketmqTemplate;
	@Autowired
	private PoolManager poolManager;
	@Autowired
	private JackPoolManager jackPoolManager;
	@Autowired
	private NnyyDragonGameService dragonGameService;
	private static final Logger LOG = LoggerFactory.getLogger(Constants.NNYY_SERVER_NAME);
	@Override
	public int getMessageId() {
		return MessageConstants.NNYY_START_GAME_INFO;
	}

	@Override
	public HttpMessageResult handler(String uuid, Map<String, String> body)
			throws Throwable {
		// 返回信息
		NnyyGameBetResult result = new NnyyGameBetResult();
		String roleid = body.get("roleid");
		double lineBet = Double.parseDouble(body.get("jetton"));//档位
		int betnum = Integer.parseInt(body.get("num"));//级别 1-9
		
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
		
		if(!betEnum || betnum < 1 || betnum > NnyyWinLineEnum.values().length){
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
		// 投注前金币
		double startbalance = usercoin;
		//总额
		double totalBet = new BigDecimal(lineBet * betnum).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		// 检查游戏币合理性
		if (usercoin < totalBet) {
			result.setRet(4);
			result.setMsg("游戏币不足");
			return result;
		}
		BetInfo betInfo = new BetInfo(lineBet, betnum,totalBet);
		// 回合id
		JackPool p = jackPoolManager.getPool();
		/* int randomMd5 = RandomUtil.getRandom(10000000, 99999999);
		String md5 = Md5Utils.md5(String.valueOf(randomMd5));
		int random = RandomUtil.getRandom(0, md5.length() - 6);
		String substring = md5.substring(random, random + 5);
		String roundId = roleid + substring;*/
		String roundId = MessageIdEnum.NNYC.getGameId() + "|" + userService.getOrderId();
		//下注
//		userService.bet(roleid, totalBet, roundId, uuid, Constants.BETTYPE1);
		
		// 查询当前赔率
		SoltGameInfo gameInfo = bgManagementServiceImp.queryGameInfo(MessageIdEnum.NNYC.getRedisName());
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
				result = gameService.doGameProcess(roleid, betInfo,userinfo, uuid,iconId);
				count ++;
				if (result.getRewardcoin() >= oddsReward - oddsReward*0.1 && result.getRewardcoin() <= oddsReward
						|| (count > 200 && result.getRewardcoin() > 0)) {
					//System.out.println("本次赔率奖池的奖励为: " + result.getRewardcoin());
					break;
				}
			}
		} else {
			result = gameService.doGameProcess(roleid, betInfo,userinfo, uuid,iconId);
			// 验证大奖金额
			double checkAmount = gameInfo.getCheckAmount();
			if (result.getRewardcoin() >= checkAmount && checkAmount > 0) {
				boolean checkBigReward = bgManagementServiceImp.checkBigReward(gameInfo,result.getRewardcoin());
				if (!checkBigReward) {
					while (true) {
						result = gameService.doGameProcess(roleid, betInfo,userinfo, uuid,iconId);
						if (result.getRewardcoin() < checkAmount) {
							break;
						}
					}
				}
			}
		}
		
		//result = gameService.doGameProcess(roleid, betInfo,userinfo, uuid,1);
		
		/*// 判断通杀
		LOG.info("POOL :" + p.getCurrentnum()  + "|" +"POOL_STATUS :" + p.getStatus() 
		+ "|" + "CURRENT_REWARD :" + result.getRewardcoin());
		if ((p.getStatus() == JackPool.STATUS_KILL && result.getRewardcoin() > 0)
				|| (p.getStatus() == JackPool.STATUS_KILL && result.getPoolGame() > 0)) {
			LOG.info("STATUS_KILL_BEFORE :" + result.getRewardcoin());
			int count = 0;
			while (true) {
				result = gameService.doGameProcess(roleid, betInfo,userinfo, uuid);
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
				result = gameService.doGameProcess(roleid, betInfo,userinfo, uuid);
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
				result = gameService.doGameProcess(roleid, betInfo,userinfo, uuid);
				count ++ ;
				if (userService.checkBigReward(betInfo.getTotalBet(), result.getRewardcoin(), p.getCurrentnum()) || count == 100) {
					LOG.info("STATUS_NORMAL_AFTER:" + result.getRewardcoin() + "|" + "COUNT : " + count);
					break;
				}
			}
		}*/
		
		// 将奖金放入奖池
		Map<String, Double> addBet = bgManagementServiceImp.addBet(2,totalBet);
		if (addBet.size() > 0) {
			poolManager.add(addBet);
		}
		
		//是否触发奖池游戏
		dragonGameService.random(result, roleid, betInfo, userinfo, uuid);
		
		//改变
		double change = NumberTool.subtract(result.getRewardcoin(), totalBet).doubleValue();
		result.setUsercoin(usercoin);
		//派彩
//		userService.payout(roleid, result.getRewardcoin(), roundId, uuid);
		result.setUsercoin(NumberTool.add(usercoin, change).doubleValue());
		
		// 记录日志
		//gameService.saveBigReward(Constants.NNYY_SERVER_NAME, betInfo, result.getRewardcoin(), roleid);
		ShowRecordResult srr = new ShowRecordResult();
		int ispool = 0;
		if (oddsReward > 0) {
			ispool = 1;
		}
		srr.setGameName(Constants.NNYY_GAME_NAME);
		srr.setDbId("DMG17-" + roundId);
		srr.setRecordType(Constants.RECORDTYPE1);
		String date = DateUtils.format(new Date(), DateUtils.fp1);
		srr.setDate(date);
		srr.setStartbalance(startbalance);
		srr.setBet(totalBet);
		srr.setReward(result.getRewardcoin());
		srr.setEndbalance(result.getUsercoin());
		srr.setGameresult(result);
		//srr.setGametime(gametime);
		srr.setRolenick(userinfo.getNickname());
		srr.setRoundId(roundId);
//		CommonLog.getInstance(restTemplate).createGameLhjLog(LogType.GAME_NNYY, NnyyConstants.SERVER_NAME, userinfo.getRoleId(), betcoin + "&" + betnum + "&" + JSON.toJSONString(result));
		GameLhjLog log = GameLhjLog.build(Constants.NNYY_GAME_NAME, "start", roleid,userinfo.getNickname(),uuid,totalBet,result.getRewardcoin(),1,ispool, JSON.toJSONString(srr));
		//rocketmqTemplate.pushLog(Tags.LHJ_GAME, JSONObject.toJSONString(log));
		WorkManager.instance().submit(updaeInventory.class, log);
		return result;
	}

}
