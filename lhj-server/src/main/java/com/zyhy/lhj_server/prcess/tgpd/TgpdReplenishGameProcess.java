package com.zyhy.lhj_server.prcess.tgpd;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.zyhy.common_lhj.BetInfo;
import com.zyhy.common_lhj.Player;
import com.zyhy.common_lhj.pool.DragonPool;
import com.zyhy.common_lhj.pool.JackPool;
import com.zyhy.common_lhj.pool.JackPoolManager;
import com.zyhy.common_lhj.process.AbstractHttpMsgProcess;
import com.zyhy.common_server.model.GameLhjLog;
import com.zyhy.common_server.result.HttpMessageResult;
import com.zyhy.common_server.util.DateUtils;
import com.zyhy.common_server.util.NumberTool;
import com.zyhy.common_server.util.RandomUtil;
import com.zyhy.lhj_server.bgmanagement.config.MessageIdEnum;
import com.zyhy.lhj_server.bgmanagement.manager.WorkManager;
import com.zyhy.lhj_server.bgmanagement.prcess.work.updaeInventory;
import com.zyhy.lhj_server.bgmanagement.service.imp.BgManagementServiceImp;
import com.zyhy.lhj_server.constants.Constants;
import com.zyhy.lhj_server.constants.MessageConstants;
import com.zyhy.lhj_server.game.ShowRecordResult;
import com.zyhy.lhj_server.game.tgpd.TgpdDragonPool;
import com.zyhy.lhj_server.game.tgpd.TgpdPoolManager;
import com.zyhy.lhj_server.game.tgpd.TgpdReplenish;
import com.zyhy.lhj_server.game.tgpd.TgpdScatterInfo;
import com.zyhy.lhj_server.prcess.result.tgpd.TgpdGameBetResult;
import com.zyhy.lhj_server.service.tgpd.TgpdGameService;
import com.zyhy.lhj_server.service.tgpd.TgpdReplenishService;
import com.zyhy.lhj_server.userservice.UserService;

/**
 * @author DPC
 * @version 创建时间：2019年2月26日 下午5:04:42
 * 
 * 补充游戏数据
 */
@Order
@Component
public class TgpdReplenishGameProcess extends AbstractHttpMsgProcess {
	@Autowired
	private BgManagementServiceImp bgManagementServiceImp;
	@Autowired
	private TgpdGameService gameService;
	@Autowired
	private TgpdReplenishService replenishService;
	@Autowired
	private UserService userService;
	//@Autowired
	//private RocketmqTemplate rocketmqTemplate;
	@Autowired
	private TgpdPoolManager poolManager;
	@Autowired
	private JackPoolManager jackPoolManager;
	//@Autowired
	//private JackPoolManager jackPoolManager;
	//private static final Logger LOG = LoggerFactory.getLogger(Constants.TGPD_SERVER_NAME);
	@Override
	public int getMessageId() {
		return MessageConstants.TGPD_REPLENISH_GAME_INFO;
	}

	@Override
	public HttpMessageResult handler(String uuid, Map<String, String> body)
			throws Throwable {
		
		TgpdGameBetResult result = new TgpdGameBetResult();
		String roleid = body.get("roleid");
		int type = Integer.parseInt(body.get("type")); // 当前游戏模式
		
		// 判断重转数据
		TgpdReplenish rep = gameService.getReplenish(roleid, uuid);
		if(rep == null){
			result.setRet(2);
			result.setMsg("没有数据");
			return result;
		}
		
		//判断是否免费游戏中
		TgpdScatterInfo bi = gameService.getFree(roleid, uuid);
		boolean freeGame = false;
		if(bi != null && bi.getNum() > 0 && bi.getNum() != 10){//正在进行免费游戏
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
		
		// 执行游戏逻辑
		result = replenishService.doGameProcess(roleid, rep, freeGame);
		
		/*// 当前的赔率
		SoltGameInfo gameInfo = bgManagementServiceImp.queryGameInfo(SoltGameNameEnum.TGPD.getRedisName());
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
		
		// 免费游戏奖励
		if (freeGame && result.getRewardcoin() > 0) {
			result.setRewardcoin(NumberTool.multiply(result.getRewardcoin(), bi.getMul()).doubleValue());
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
		if (!freeGame && result.isFree()) {
			if (bi != null && bi.getNum() == 10) {
				//freeGame = false;
			} else {
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
				gameService.saveGameInfoCache(scatter, Constants.REDIS_LHJ_TGPD_FREE, roleid, userinfo, uuid);
				result.setMul(random);
				result.setFreeNum(10);
				result.setScatter(scatter);
			}
		}
		
		//更新免费游戏总奖励
		if(freeGame ){
			bi.setGold(NumberTool.add(bi.getGold(), result.getRewardcoin()).doubleValue());
			bi.setSingleReward(result.getRewardcoin());
			gameService.saveGameInfoCache(bi , Constants.REDIS_LHJ_TGPD_FREE, roleid, userinfo, uuid);
			result.setFreeNum(bi.getNum());
			result.setMul(bi.getMul());
			result.setScatter(bi);
		}
		
		// 删除免费信息
		if(bi != null && bi.getNum() == 0 ){
			gameService.delFreeInfoCache(roleid,Constants.REDIS_LHJ_TGPD_FREE, uuid);
		}
		
		//累加掉落奖励
		rep.setDropReward(NumberTool.add(rep.getDropReward(), 
				NumberTool.add(result.getRewardcoin(), result.getPoolRewardcoin())).doubleValue());
		gameService.saveGameInfoCache(rep, Constants.REDIS_LHJ_TGPD_REPLENISH, roleid, userinfo, uuid);
		result.setRewardcoin(0);
		
		// 删除补充数据
		if(!result.isReplenish()){
			result.setRewardcoin(rep.getDropReward());
			gameService.delFreeInfoCache(roleid,Constants.REDIS_LHJ_TGPD_REPLENISH, uuid);
		}
		
		//金币变化
		double change = NumberTool.subtract(
				NumberTool.add(result.getRewardcoin(), result.getPoolRewardcoin()), 0.0).doubleValue();
		// 回合id
		JackPool p = jackPoolManager.getPool();
		String roundId = MessageIdEnum.TGPD.getGameId() + "|" + String.valueOf(p.getOrderNum());
		// 免费游戏下注
//		userService.bet(roleid, 0, roundId, uuid, Constants.BETTYPE0);
		// 派彩
//		userService.payout(roleid, NumberTool.add(result.getRewardcoin(), result.getPoolRewardcoin()).doubleValue(), roundId, uuid);
		result.setUsercoin(NumberTool.add(usercoin, change).doubleValue());
		
		//记录日志
		//gameService.saveBigReward(Constants.TGPD_SERVER_NAME, rep.getBetInfo(),result.getRewardcoin(), roleid);
		ShowRecordResult srr = new ShowRecordResult();
		int ispool = 0;
		srr.setGameName(Constants.TGPD_GAME_NAME);
		srr.setDbId("DMG17_1-" + roundId);
		srr.setRecordType(Constants.RECORDTYPE2);
		String date = DateUtils.format(new Date(), DateUtils.fp1);
		srr.setDate(date);
		srr.setStartbalance(startbalance);
		srr.setBet(0);
		srr.setReward(NumberTool.add(result.getRewardcoin(), result.getPoolRewardcoin()).doubleValue());
		srr.setEndbalance(result.getUsercoin());
		srr.setGameresult(result);
		//srr.setGametime(gametime);
		srr.setRolenick(userinfo.getNickname());
		srr.setRoundId(roundId);
		GameLhjLog log = GameLhjLog.build(Constants.TGPD_GAME_NAME, "dorp", roleid,userinfo.getNickname(),uuid,0, NumberTool.add(result.getRewardcoin(), result.getPoolRewardcoin()).doubleValue() + 1,2,ispool, JSON.toJSONString(srr));
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
		gameService.saveGameInfoCache(result, Constants.REDIS_LHJ_TGPD_GAMEINFO, roleid, userinfo, uuid);
		return result;
	}

}
