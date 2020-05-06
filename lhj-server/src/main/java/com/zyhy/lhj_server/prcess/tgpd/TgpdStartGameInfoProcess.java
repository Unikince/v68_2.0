/**
 * 
 */
package com.zyhy.lhj_server.prcess.tgpd;

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
import com.zyhy.common_lhj.pool.DragonPool;
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
import com.zyhy.lhj_server.game.ShowRecordResult;
import com.zyhy.lhj_server.game.TgpdOddsEnum;
import com.zyhy.lhj_server.game.tgpd.TgpdDragonPool;
import com.zyhy.lhj_server.game.tgpd.TgpdPoolManager;
import com.zyhy.lhj_server.game.tgpd.TgpdReplenish;
import com.zyhy.lhj_server.game.tgpd.TgpdScatterInfo;
import com.zyhy.lhj_server.prcess.result.tgpd.TgpdGameBetResult;
import com.zyhy.lhj_server.service.tgpd.TgpdGameService;
import com.zyhy.lhj_server.userservice.UserService;

/**
 * @author linanjun
 * 开始游戏
 */
@Order
@Component
public class TgpdStartGameInfoProcess extends AbstractHttpMsgProcess{
	@Autowired
	private TgpdOpenGamePanlProcess openGamePanlProcess;
	@Autowired
	private BgManagementServiceImp bgManagementServiceImp;
	@Autowired
	private TgpdGameService gameService;
	@Autowired
	private UserService userService;
	//@Autowired
	//private RocketmqTemplate rocketmqTemplate;
	@Autowired
	private TgpdPoolManager poolManager;
	@Autowired
	private JackPoolManager jackPoolManager;
	private static final Logger LOG = LoggerFactory.getLogger(Constants.TGPD_SERVER_NAME);
	@Override
	public int getMessageId() {
		return MessageConstants.TGPD_START_GAME_INFO;
	}
	@Override
	public HttpMessageResult handler(String uuid, Map<String, String> body)
			throws Throwable {
		// 返回信息
		TgpdGameBetResult result = new TgpdGameBetResult();
		String roleid = body.get("roleid");
		double lineBet = Double.parseDouble(body.get("jetton"));// 单注金额
		int betnum = Integer.parseInt(body.get("num"));// 注数
		int type = Integer.parseInt(body.get("type")); // 当前游戏模式
		
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
		
		if(!betEnum || betnum < 1 || betnum > 5){
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
		//判断是否在掉落
		TgpdReplenish rep = gameService.getReplenish(roleid, uuid);
		if(rep != null){
			result.setRet(2);
			result.setMsg("掉落中");
			return result;
		}
		//判断是否免费游戏中
		TgpdScatterInfo bi = gameService.getFree(roleid, uuid);
		if(bi != null  ){
			result.setRet(2);
			result.setMsg("免费游戏中");
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
		// 回合id
		//JackPool p = jackPoolManager.getPool();
		/* int randomMd5 = RandomUtil.getRandom(10000000, 99999999);
		String md5 = Md5Utils.md5(String.valueOf(randomMd5));
		int random2 = RandomUtil.getRandom(0, md5.length() - 6);
		String substring = md5.substring(random2, random2 + 5);
		String roundId = roleid + substring;*/
		String roundId = MessageIdEnum.TGPD.getGameId() + "|" + userService.getOrderId();
		// 下注
//		userService.bet(roleid, totalBet, roundId, uuid, Constants.BETTYPE1);
		BetInfo betInfo = new BetInfo(lineBet, betnum ,totalBet);
		
		// 查询当前赔率
		SoltGameInfo gameInfo = bgManagementServiceImp.queryGameInfo(MessageIdEnum.TGPD.getRedisName());
		double odds = bgManagementServiceImp.queryLhjCurrentOdds(gameInfo);
		//System.out.println("gameInfo" + gameInfo);
		//System.out.println("当前游戏的赔率: " + odds);
		int iconId = (type*10) + TgpdOddsEnum.getIdByOdds(odds);
		//System.out.println("使用第几套图标" + iconId);
		if (iconId == 0) {
			iconId = (type*10) + 2;
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
				iconId = (type*10) + 3;
			}
		}
		//System.out.println("赔率奖池的奖励为为=========>" + oddsReward);
		//System.out.println("当前使用第几套图标: " + iconId);
		// 执行游戏逻辑
		if (oddsReward > 0) {
			int count = 0;
			while (true) {
				result = gameService.doGameProcess(roleid, betInfo,false,type,iconId);
				count ++;
				if (result.getRewardcoin() >= oddsReward - oddsReward*0.1 && result.getRewardcoin() <= oddsReward
						|| (count > 200 && result.getRewardcoin() > 0)) {
					//System.out.println("本次赔率奖池的奖励为: " + result.getRewardcoin());
					break;
				}
			}
		} else {
			result = gameService.doGameProcess(roleid, betInfo,false,type,iconId);
			
			if (result.getRewardcoin() > 0) {
				if (result.getRewardcoin()/(lineBet*0.1*betnum) > 100) {
					int count = 0;
					while (true) {
						result = gameService.doGameProcess(roleid, betInfo,false,type,iconId);
						count ++ ;
						if ((result.getRewardcoin()/(lineBet*0.1*betnum) < 100)  || count == 100) {
							break;
						}
					}
				}
			}
			
		}
		
		// 执行游戏逻辑
		//result = gameService.doGameProcess(roleid, betInfo,false,type);
		
		/*if (result.getRewardcoin() > 0) {
			System.out.println("本局总奖励 :" + result.getRewardcoin());
			System.out.println("本局总投注的0.1 :" + (lineBet*0.1*betnum));
			System.out.println("本局赢赏倍率 :" + result.getRewardcoin()/(lineBet*0.1*betnum));
			if (result.getRewardcoin()/(lineBet*0.1*betnum) > 100) {
				System.out.println("正常游戏中大于100倍的赢赏倍率 :" + result.getRewardcoin()/(lineBet*0.1*betnum));
				int count = 0;
				while (true) {
					result = gameService.doGameProcess(roleid, betInfo,false,type);
					count ++ ;
					if ((result.getRewardcoin()/(lineBet*0.1*betnum) < 100)  || count == 100) {
						System.out.println("重新取的赢赏倍率 :" + result.getRewardcoin()/(lineBet*0.1*betnum));
						break;
					}
				}
			}
		}*/
		
		/*// 当前使用那套图标
		result.setIcon(iconId);
		// 当前的赔率
		System.out.println("总派彩" + gameInfo.getTotalPay());
		System.out.println("总投注" + gameInfo.getTotalBet());
		double odds2 = gameInfo.getTotalPay()/gameInfo.getTotalBet();
		double odds1 = 0;
		if (odds2 > 0) {
			odds1 = new BigDecimal(odds2).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		}
		result.setOdds(odds1);
		result.setTotalBet(gameInfo.getTotalBet());
		result.setTotalReward(gameInfo.getTotalPay());
		System.out.println("总赔率" + odds1);*/
		
		
		/*// 判断通杀
		LOG.info("POOL :" + p.getCurrentnum()  + "|" +"POOL_STATUS :" + p.getStatus() 
		+ "|" + "CURRENT_REWARD :" + result.getRewardcoin());
		if ((p.getStatus() == JackPool.STATUS_KILL && result.getRewardcoin() > 0) 
				|| (p.getStatus() == JackPool.STATUS_KILL &&  result.getPoolCount() > 0) 
				|| (p.getStatus() == JackPool.STATUS_KILL &&  result.isFree())) {
			LOG.info("STATUS_KILL_BEFORE :" + result.getRewardcoin());
			int count = 0;
			while (true) {
				result = gameService.doGameProcess(roleid, betInfo,false,type);
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
				result = gameService.doGameProcess(roleid, betInfo,false,type);
				count ++ ;
				if ((result.getRewardcoin() > 0  
						&& userService.checkBigReward(betInfo.getTotalBet(), result.getRewardcoin(), p.getCurrentnum()))|| count == 100) {
					LOG.info("STATUS_WIN_AFTER:" + result.getRewardcoin() + "|" + "COUNT : " + count);
					break;
				}
			}
		}
		
		// 正常模式控制中奖率
		if(result.getRewardcoin() > 0 ){
			int randomNum = RandomUtil.getRandom(0, 10000);
			int kill = 5000;
			if(randomNum < kill){
				int count = 0;
				while (true) {
					result = gameService.doGameProcess(roleid, betInfo,false,type);
					count ++ ;
					if (result.getRewardcoin() == 0 || count == 100) {
						LOG.info("STATUS_KILL_AFTER :" + result.getRewardcoin() + "|" + "COUNT : " + count);
						break;
					}
				}
			}
		}
		
		// 正常模式
		if (result.getRewardcoin() > 0 && 
				!userService.checkBigReward(betInfo.getTotalBet(), result.getRewardcoin(), p.getCurrentnum())) {
			LOG.info("STATUS_NORMAL_BEFORE :" + result.getRewardcoin());
			int count = 0;
			while (true) {
				result = gameService.doGameProcess(roleid, betInfo,false,type);
				count ++ ;
				if (userService.checkBigReward(betInfo.getTotalBet(), result.getRewardcoin(), p.getCurrentnum()) || count == 100) {
					LOG.info("STATUS_NORMAL_AFTER:" + result.getRewardcoin() + "|" + "COUNT : " + count);
					break;
				}
			}
		}*/
		
		// 将奖金放入奖池
		Map<String, Double> addBet = bgManagementServiceImp.addBet(18,totalBet);
		if (addBet.size() > 0) {
			poolManager.add(addBet);
		}
		
		// 获取奖池奖励
		if (result.isPoolReward()) {
			DragonPool pool = poolManager.getPool();
			switch (result.getPoolCount()) {
			case 1:
				result.setPoolRewardcoin(pool.getMini());
				poolManager.init(TgpdDragonPool.MINI);
				break;
			case 2:
				result.setPoolRewardcoin(pool.getMinor());
				poolManager.init(TgpdDragonPool.MINOR);
				break;
			case 3:
				result.setPoolRewardcoin(pool.getMajor());
				poolManager.init(TgpdDragonPool.MAJOR);
				break;
			case 4:
				result.setPoolRewardcoin(pool.getGrand());
				poolManager.init(TgpdDragonPool.GRAND);
				break;
			default:
				break;
			}
		}
		
		// 是否有免费游戏
		if (result.isFree()) {
			TgpdScatterInfo scatter = new TgpdScatterInfo(betInfo);
			int total = RandomUtil.getRandom(1, 100);
			int random = 1;
			if (total > 30) {
				random = 2;
			}
			if (total > 55) {
				random = 3;
			}
			if (total > 75) {
				random = 4;
			}
			if (total > 90) {
				random = 5;
			}
			scatter.setNum(10);
			scatter.setSingleReward(0);
			scatter.setGold(0);
			scatter.setMul(random);
			scatter.setRoundId(roundId);
			gameService.saveGameInfoCache(scatter, Constants.REDIS_LHJ_TGPD_FREE, roleid, userinfo, uuid);
			result.setMul(random);
			result.setFreeNum(10);
			result.setScatter(scatter);
		}
		
		// 是否掉落
		if (result.isReplenish()) {
			TgpdReplenish newRep = new TgpdReplenish(betInfo);
			newRep.setDropReward(result.getRewardcoin());
			gameService.saveGameInfoCache(newRep, Constants.REDIS_LHJ_TGPD_REPLENISH, roleid, userinfo, uuid);
			result.setRewardcoin(0);
		}
		
		//金币变化
		double change = NumberTool.subtract(
				NumberTool.add(result.getRewardcoin(), result.getPoolRewardcoin()), totalBet).doubleValue();
		
		// 派彩
//		userService.payout(roleid, NumberTool.add(result.getRewardcoin(), result.getPoolRewardcoin()).doubleValue(), roundId, uuid);
		result.setUsercoin(NumberTool.add(usercoin, change).doubleValue());
		
		// 记录日志
		//gameService.saveBigReward(Constants.TGPD_SERVER_NAME, betInfo, result.getRewardcoin(), roleid);
		ShowRecordResult srr = new ShowRecordResult();
		int ispool = 0;
		if (oddsReward > 0) {
			ispool = 1;
		}
		srr.setGameName(Constants.TGPD_GAME_NAME);
		srr.setDbId("DMG17-" + roundId);
		srr.setRecordType(Constants.RECORDTYPE1);
		String date = DateUtils.format(new Date(), DateUtils.fp1);
		srr.setDate(date);
		srr.setStartbalance(startbalance);
		srr.setBet(totalBet);
		srr.setReward(NumberTool.add(result.getRewardcoin(), result.getPoolRewardcoin()).doubleValue());
		srr.setEndbalance(result.getUsercoin());
		srr.setGameresult(result);
		//srr.setGametime(gametime);
		srr.setRolenick(userinfo.getNickname());
		srr.setRoundId(roundId);
		GameLhjLog log = GameLhjLog.build(Constants.TGPD_GAME_NAME, "start", roleid,userinfo.getNickname(),uuid,totalBet,NumberTool.add(result.getRewardcoin(), result.getPoolRewardcoin()).doubleValue(),1,ispool, JSON.toJSONString(srr));
		//rocketmqTemplate.pushLog(Tags.LHJ_GAME, JSONObject.toJSONString(log));
		WorkManager.instance().submit(updaeInventory.class, log);
		// 更新游戏信息
		TgpdGameBetResult oldResult = gameService.getGameInfo(roleid, uuid);
		if (oldResult != null) {
			result.setPropsNum(result.getPropsNum() + oldResult.getPropsNum());
		}
		// 判断是否升级
		if (result.getPropsNum() >= 15) {
			if (type == 3) {
				result.setType(1);
			}else {
				result.setType(NumberTool.add(type, 1).intValue());
			}
			result.setPropsNum(0);
		} else {
			if (oldResult != null) {
				result.setType(oldResult.getType());
			} else {
				result.setType(type);
			}
			
		}
		//保存本局游戏信息
		gameService.saveGameInfoCache(result, Constants.REDIS_LHJ_TGPD_GAMEINFO, roleid , userinfo, uuid);
		return result;
	}

}
