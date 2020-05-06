package com.zyhy.lhj_server.prcess.bqtp;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.zyhy.common_lhj.BetInfo;
import com.zyhy.common_lhj.Player;
import com.zyhy.common_lhj.WindowInfo;
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
import com.zyhy.lhj_server.game.bqtp.BqtpIconEnum;
import com.zyhy.lhj_server.game.bqtp.BqtpReplenish;
import com.zyhy.lhj_server.game.bqtp.BqtpScatterInfo;
import com.zyhy.lhj_server.prcess.result.bqtp.BqtpGameBetResult;
import com.zyhy.lhj_server.service.bqtp.BqtpGameService;
import com.zyhy.lhj_server.service.bqtp.BqtpReplenishService;
import com.zyhy.lhj_server.userservice.UserService;

/**
 * @author DPC
 * @version 创建时间：2019年2月26日 下午5:04:42
 * 
 * 补充游戏数据
 */
@Order
@Component
public class BqtpReplenishGameProcess extends AbstractHttpMsgProcess {
	@Autowired
	private BgManagementServiceImp bgManagementServiceImp;
	@Autowired
	private BqtpGameService gameService;
	@Autowired
	private BqtpReplenishService replenishService;
	@Autowired
	private UserService userService;
	//@Autowired
	//private RocketmqTemplate rocketmqTemplate;
	@Autowired
	private JackPoolManager jackPoolManager;
	private static final Logger LOG = LoggerFactory.getLogger(Constants.BQTP_SERVER_NAME);
	@Override
	public int getMessageId() {
		return MessageConstants.BQTU_REPLENISH_GAME_INFO;
	}

	@Override
	public HttpMessageResult handler(String uuid, Map<String, String> body)
			throws Throwable {
		BqtpGameBetResult result = new BqtpGameBetResult();
		String roleid = body.get("roleid");
		BqtpReplenish rep = replenishService.getReplenishData(roleid, uuid);
		if(rep == null){
			result.setRet(2);
			result.setMsg("没有数据");
			return result;
		}
		
		//判断是否免费游戏中
		BqtpScatterInfo bi = gameService.getData(roleid, uuid);
		boolean freeGame = false;
		if(bi != null && bi.getNum() > 0){//正在进行免费游戏
			freeGame = true;
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
		
		BetInfo betInfo = rep.getBetInfo();
		
		// 查询当前赔率
		SoltGameInfo gameInfo = bgManagementServiceImp.queryGameInfo(MessageIdEnum.BQTP.getRedisName());
		double odds = bgManagementServiceImp.queryLhjCurrentOdds(gameInfo);
		//System.out.println("gameInfo" + gameInfo);
		//System.out.println("当前游戏的赔率: " + gameInfo.getOdds());
		int iconId = GameOddsEnum.getIdByOdds(odds);
		//System.out.println("使用第几套图标" + iconId);
		if (iconId == 0) {
			iconId = 4;
			//System.out.println("没有查询到赔率,使用第"+  iconId +"套图标" );
		}
		
		result = replenishService.doGameProcess(roleid, rep,freeGame,iconId);
		// 验证大奖金额
		double checkAmount = gameInfo.getCheckAmount();
		if (result.getRewardcoin() >= checkAmount && checkAmount > 0) {
			boolean checkBigReward = bgManagementServiceImp.checkBigReward(gameInfo,result.getRewardcoin());
			if (!checkBigReward) {
				while (true) {
					result = replenishService.doGameProcess(roleid, rep,freeGame,iconId);
					if (result.getRewardcoin() < checkAmount) {
						break;
					}
				}
			}
		}
		
		//result = replenishService.doGameProcess(roleid, rep,freeGame,1);
		
		/*// 判断通杀
		JackPool p = jackPoolManager.getPool();
		LOG.info("POOL :" + p.getCurrentnum()  + "|" +"POOL_STATUS :" + p.getStatus() 
		+ "|" + "CURRENT_REWARD :" + result.getRewardcoin());
		if ((p.getStatus() == JackPool.STATUS_KILL && result.getRewardcoin() > 0)
				|| (p.getStatus() == JackPool.STATUS_KILL && result.isScatter())) {
			LOG.info("STATUS_KILL_BEFORE :" + result.getRewardcoin());
			int count = 0;
			while (true) {
				result = replenishService.doGameProcess(roleid, rep,freeGame);
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
				result = replenishService.doGameProcess(roleid, rep,freeGame);
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
				result = replenishService.doGameProcess(roleid, rep,freeGame);
				count ++ ;
				if (userService.checkBigReward(betInfo.getTotalBet(), result.getRewardcoin(), p.getCurrentnum()) || count == 100) {
					LOG.info("STATUS_NORMAL_AFTER:" + result.getRewardcoin() + "|" + "COUNT : " + count);
					break;
				}
			}
		}*/
		
		
		// 计算免费游戏奖励
		if (freeGame && bi != null) {
			// 计算连续中奖次数
			if(result.getRewardcoin() > 0){
				List<List<WindowInfo>> win = result.getWin();
				int scatter = 0;
				int other = 0;
				for (List<WindowInfo> list : win) {
					for (WindowInfo wl : list) {
						if (wl != null) {
							if (wl.getIcon() == BqtpIconEnum.SCATTER) {
								scatter ++;
							} else {
								other ++;
							}
						}
					}
				}
				if (scatter == 0 || other > 0) {
					bi.setCount(bi.getCount() + 1);
				} else {
					bi.setCount(1);
				}
			}else{
				bi.setCount(1);
			}
			if (bi.getCount() > 1 && bi.getCount() < 11) {
				result.setRewardcoin(NumberTool.multiply(result.getRewardcoin(),bi.getCount()).doubleValue());
			}
		}
		
		//更新免费游戏总奖励
		if(bi != null ){
			bi.setGold(NumberTool.add(bi.getGold(), result.getRewardcoin()).doubleValue());
			bi.setSingleReward(result.getRewardcoin());
			gameService.saveFreeInfoCache(bi, roleid, userinfo, uuid);
			result.setScatterInfo(bi);
			result.setScatterNum(bi.getNum());
		}
		
		if(bi != null && bi.getNum() == 0 ){
			gameService.delFreeInfoCache(roleid, uuid);
		}
		
		// 补充数据
		if(result.isReplenish()){
			replenishService.saveReplenishInfoCache(result.getRep(),roleid, uuid);
		} else {
			replenishService.delReplenishInfoCache(roleid, uuid);
		}
		
//		//是否要进行免费游戏
//		if(result.isScatter() && !freeGame){
//			ScatterInfo sinfo = new ScatterInfo(betInfo);
//			sinfo.setNum(result.getScatterNum());
//			sinfo.setGold(0);
//			gameService.save(sinfo, roleid);
//		}
		
		//金币变化
		double change = NumberTool.subtract(result.getRewardcoin(), 0.0).doubleValue();
		// 回合id
		/* int randomMd5 = RandomUtil.getRandom(10000000, 99999999);
		String md5 = Md5Utils.md5(String.valueOf(randomMd5));
		int random = RandomUtil.getRandom(0, md5.length() - 6);
		String substring = md5.substring(random, random + 5);
		String roundId = roleid + substring;*/
		String roundId = MessageIdEnum.BQTP.getGameId() + "|" + userService.getOrderId();
		// 免费游戏下注
//		userService.bet(roleid, 0, roundId, uuid, Constants.BETTYPE0);
		// 派彩
//		userService.payout(roleid, result.getRewardcoin(), roundId, uuid);
		result.setUsercoin(NumberTool.add(usercoin, change).doubleValue());
		
		// 记录日志
		//gameService.saveBigReward(Constants.BQTP_SERVER_NAME, rep.getBetInfo(),result.getRewardcoin(), roleid);
		ShowRecordResult srr = new ShowRecordResult();
		int ispool = 0;
		srr.setGameName(Constants.BQTP_GAME_NAME);
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
		GameLhjLog log = GameLhjLog.build(Constants.BQTP_GAME_NAME, "drop", roleid,userinfo.getNickname(),uuid,0,result.getRewardcoin() + 1,2,ispool,JSON.toJSONString(srr));
		//rocketmqTemplate.pushLog(Tags.LHJ_GAME, JSONObject.toJSONString(log));
		WorkManager.instance().submit(updaeInventory.class, log);
		return result;
	}

}
