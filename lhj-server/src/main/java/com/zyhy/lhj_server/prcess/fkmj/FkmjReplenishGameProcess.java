package com.zyhy.lhj_server.prcess.fkmj;

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
import com.zyhy.lhj_server.game.fkmj.FkmjBonusInfo;
import com.zyhy.lhj_server.game.fkmj.FkmjReplenish;
import com.zyhy.lhj_server.prcess.result.fkmj.FkmjGameBetResult;
import com.zyhy.lhj_server.service.fkmj.FkmjGameService;
import com.zyhy.lhj_server.service.fkmj.FkmjReplenishService;
import com.zyhy.lhj_server.userservice.UserService;

/**
 * @author DPC
 * @version 创建时间：2019年2月26日 下午5:04:42
 * 
 * 补充游戏数据
 */
@Order
@Component
public class FkmjReplenishGameProcess extends AbstractHttpMsgProcess {
	@Autowired
	private BgManagementServiceImp bgManagementServiceImp;
	@Autowired
	private FkmjGameService gameService;
	@Autowired
	private FkmjReplenishService replenishService;
	@Autowired
	private UserService userService;
	//@Autowired
	//private RocketmqTemplate rocketmqTemplate;
	@Autowired
	private JackPoolManager jackPoolManager;
	private static final Logger LOG = LoggerFactory.getLogger(Constants.FKMJ_SERVER_NAME);
	@Override
	public int getMessageId() {
		return MessageConstants.FKMJ_REPLENISH_GAME_INFO;
	}

	@Override
	public HttpMessageResult handler(String uuid, Map<String, String> body)
			throws Throwable {
		FkmjGameBetResult result = new FkmjGameBetResult();
		String roleid = body.get("roleid");
		FkmjReplenish rep = gameService.getReplenishData(roleid, uuid);
		if(rep == null){
			result.setRet(2);
			result.setMsg("没有数据");
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
		// 赌注信息
		BetInfo betInfo = rep.getBetInfo();
		
		//判断是否免费游戏中
		FkmjBonusInfo bi = gameService.getFree(roleid, uuid);
		boolean freeGame = false;
		if(bi != null && bi.getNum() != 15){//正在进行免费游戏
			betInfo = bi.getBetInfo();
			freeGame = true;
		}
		
		// 查询当前赔率
		SoltGameInfo gameInfo = bgManagementServiceImp.queryGameInfo(MessageIdEnum.FKMJ.getRedisName());
		double odds = bgManagementServiceImp.queryLhjCurrentOdds(gameInfo);
		//System.out.println("gameInfo" + gameInfo);
		//System.out.println("当前游戏的赔率: " + gameInfo.getOdds());
		int iconId = GameOddsEnum.getIdByOdds(odds);
		//System.out.println("使用第几套图标" + iconId);
		if (iconId == 0) {
			iconId = 4;
			//System.out.println("没有查询到赔率,使用第"+  iconId +"套图标" );
		}
		
		result = replenishService.doGameProcess(roleid, betInfo , freeGame , bi,iconId);
		// 验证大奖金额
		double checkAmount = gameInfo.getCheckAmount();
		if (result.getRewardcoin() >= checkAmount && checkAmount > 0) {
			boolean checkBigReward = bgManagementServiceImp.checkBigReward(gameInfo,result.getRewardcoin());
			if (!checkBigReward) {
				while (true) {
					result = replenishService.doGameProcess(roleid, betInfo , freeGame , bi,iconId);
					if (result.getRewardcoin() < checkAmount) {
						break;
					}
				}
			}
		}
		
		//result = replenishService.doGameProcess(roleid, betInfo , freeGame , bi,1);
		
		/*// 判断通杀
		JackPool p = jackPoolManager.getPool();
		LOG.info("POOL :" + p.getCurrentnum()  + "|" +"POOL_STATUS :" + p.getStatus() 
		+ "|" + "CURRENT_REWARD :" + result.getRewardcoin());
		if (p.getStatus() == JackPool.STATUS_KILL && result.getRewardcoin() > 0) {
			LOG.info("STATUS_KILL_BEFORE :" + result.getRewardcoin());
			int count = 0;
			while (true) {
				result = replenishService.doGameProcess(roleid, betInfo , freeGame , bi);
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
				result = replenishService.doGameProcess(roleid, betInfo , freeGame , bi);
				count ++ ;
				if ((result.getRewardcoin() > 0  
						&& userService.checkBigReward(betInfo.getTotalBet(), result.getRewardcoin(), p.getCurrentnum()))|| count == 100) {
					LOG.info("STATUS_WIN_AFTER:" + result.getRewardcoin() + "|" + "COUNT : " + count);
					break;
				}
			}
		}
		
		// 正常模式控制中奖率
		if(result.getRewardcoin() > 0 && betInfo.getNum() > 2){
			int randomNum = RandomUtil.getRandom(0, 10000);
			int kill = 0;
			if(betInfo.getNum() > 5){
				kill = 4500;
			}else{
				kill = 1500;
			}
			if(randomNum < kill){
				int count = 0;
				while (true) {
					result = replenishService.doGameProcess(roleid, betInfo , freeGame , bi);
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
				result = replenishService.doGameProcess(roleid, betInfo , freeGame , bi);
				count ++ ;
				if (userService.checkBigReward(betInfo.getTotalBet(), result.getRewardcoin(), p.getCurrentnum()) || count == 100) {
					LOG.info("STATUS_NORMAL_AFTER:" + result.getRewardcoin() + "|" + "COUNT : " + count);
					break;
				}
			}
		}*/
		
		// 更新中免费游戏时掉落的奖励
		if (bi != null && bi.getNum() == 15) {
			bi.setReward(NumberTool.add(bi.getReward(), result.getRewardcoin()).doubleValue());
			gameService.saveFree(bi, roleid, userinfo, uuid);
			result.setTotalRewardcoin(result.getRewardcoin());
			result.setRewardcoin(0);
		}
		
		// 更新免费中游戏总奖励
		if(freeGame){
			if(result.getFreeRewardcoin() > 0){
				bi.setCount(bi.getCount() + 1);
				if (bi.getCount() >= 5 ) {
					bi.setCount(5);
				}
			}else{
				bi.setCount(1);
			}
			bi.setGold(bi.getGold() + result.getFreeRewardcoin());
			gameService.saveFree(bi, roleid, userinfo, uuid);
			result.setBonusNum(bi.getNum());
			result.setBonusInfo(bi);
		}
		
		
		// 补充数据
		if(result.isDrop()){
			rep.setReplenish(true);
			gameService.saveReplenish(rep,roleid, uuid);
		} else {
			gameService.deleteReplenish(roleid, uuid);
		}
		
		// 是否要进行免费游戏
		//if(result.isBonus() && !freeGame){
			//FkmjBonusInfo binfo = new FkmjBonusInfo(betInfo);
			//binfo.setNum(15);
			//binfo.setGold(0);
			//binfo.setCount(1);
			//gameService.saveFree(binfo, roleid);
		//}
		
		
		if(bi != null && bi.getNum() == 0 && !result.isDrop()){
			result.setTotalRewardcoin(bi.getReward());
			result.setRewardcoin(bi.getReward() + bi.getGold());
			gameService.delFree(roleid, uuid);
		}
		
		//金币变化
		double change = NumberTool.subtract(result.getRewardcoin(), 0.0).doubleValue();
		// 回合id
		/* int randomMd5 = RandomUtil.getRandom(10000000, 99999999);
		String md5 = Md5Utils.md5(String.valueOf(randomMd5));
		int random = RandomUtil.getRandom(0, md5.length() - 6);
		String substring = md5.substring(random, random + 5);
		String roundId = roleid + substring;*/
		String roundId = MessageIdEnum.FKMJ.getGameId() + "|" + userService.getOrderId();
		// 免费游戏下注
//		userService.bet(roleid, 0, roundId, uuid, Constants.BETTYPE0);
		// 派彩
//		userService.payout(roleid, result.getRewardcoin(), roundId, uuid);
		result.setUsercoin(NumberTool.add(usercoin, change).doubleValue());
		
		// 记录日志
		//gameService.saveBigReward(Constants.FKMJ_SERVER_NAME, betInfo, result.getRewardcoin(),roleid);
		ShowRecordResult srr = new ShowRecordResult();
		int ispool = 0;
		srr.setGameName(Constants.FKMJ_GAME_NAME);
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
		GameLhjLog log = GameLhjLog.build(Constants.FKMJ_GAME_NAME, "drop", roleid,userinfo.getNickname(),uuid,0,result.getRewardcoin() + 1,2,ispool, JSON.toJSONString(srr));
		//rocketmqTemplate.pushLog(Tags.LHJ_GAME, JSONObject.toJSONString(log));
		WorkManager.instance().submit(updaeInventory.class, log);
		return result;
	}

}
