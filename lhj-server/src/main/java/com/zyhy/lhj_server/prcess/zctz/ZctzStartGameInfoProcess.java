/**
 * 
 */
package com.zyhy.lhj_server.prcess.zctz;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.dmg.server.common.model.dto.GameRecordDTO;
import com.dmg.server.common.util.RoundIdUtils;
import com.google.common.collect.Lists;
import com.zyhy.common_lhj.BetInfo;
import com.zyhy.common_lhj.Player;
import com.zyhy.common_lhj.process.AbstractHttpMsgProcess;
import com.zyhy.common_server.result.HttpMessageResult;
import com.zyhy.common_server.util.DateUtils;
import com.zyhy.common_server.util.NumberTool;
import com.zyhy.lhj_server.bgmanagement.config.MessageIdEnum;
import com.zyhy.lhj_server.bgmanagement.entity.SoltGameInfo;
import com.zyhy.lhj_server.bgmanagement.service.imp.BgManagementServiceImp;
import com.zyhy.lhj_server.constants.Constants;
import com.zyhy.lhj_server.constants.MessageConstants;
import com.zyhy.lhj_server.game.ShowRecordResult;
import com.zyhy.lhj_server.game.zctz.ZctzWinLineEnum;
import com.zyhy.lhj_server.prcess.result.zctz.ZctzGameBetResult;
import com.zyhy.lhj_server.service.zctz.ZctzGameService;
import com.zyhy.lhj_server.userservice.UserService;

/**
 * @author linanjun
 * 开始游戏
 */
@Order
@Component
public class ZctzStartGameInfoProcess extends AbstractHttpMsgProcess{
	@Autowired
	private BgManagementServiceImp bgManagementServiceImp;
	@Autowired
	private ZctzOpenGamePanlProcess openGamePanlProcess;
	@Autowired
	private ZctzGameService gameService;
	@Autowired
	private UserService userService;
	// 上一局使用图标序列id
	private int lastRoundIconId = 0; 
	private static final Logger LOG = LoggerFactory.getLogger(ZctzStartGameInfoProcess.class);
	@Override
	public int getMessageId() {
		return MessageConstants.ZCTZ_START_GAME_INFO;
	}

	@Override
	public HttpMessageResult handler(String uuid, Map<String, String> body)
			throws Throwable {
		// 接收信息
		String roleid = body.get("roleid");
		double betcoin = Double.parseDouble(body.get("jetton"));//档位
		int betnum = Integer.parseInt(body.get("num"));//级别 1-9
		// 返回信息
		ZctzGameBetResult result = new ZctzGameBetResult();
		
		// 判断下注信息
		Map<Double, Integer> betList = openGamePanlProcess.betenum;
		if (betList.size() <= 0) {
			result.setRet(2);
			result.setMsg("投注信息错误");
			return result;
		}
		boolean betEnum = false;
		if (betList.containsKey(betcoin)) {
			betEnum = true;
		}
		
		if(!betEnum || betnum < 1 || betnum > ZctzWinLineEnum.values().length){
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
		
		//总投注额度
		double m = 0;
		// 玩家游戏币
		double usercoin = userinfo.getGold();
		// 投注前金币
		double startbalance = usercoin;
		//总额
		m = new BigDecimal(betcoin * betnum).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		// 检查游戏币合理性
		if (usercoin < m) {
			result.setRet(4);
			result.setMsg("游戏币不足");
			return result;
		}
		
		// 下注信息
		BetInfo betInfo = new BetInfo(betcoin, betnum, m);
		
		// 回合id
		String roundId = MessageIdEnum.ZCJB.getGameId() + "|" + userService.getOrderId();
		
		//下注
		boolean betResult = userService.bet(roleid, betInfo.total(), roundId, uuid, Constants.BETTYPE1,MessageIdEnum.ZCJB.getBgId());
		if (!betResult) {
			result.setRet(2);
			result.setMsg("下注失败");
			return result;
		}
		
        // 本局游戏使用的图标id
        int iconId = 0;
        // 点控
        boolean pointControl = false; 
        int pointControlModel = bgManagementServiceImp.getPointControlModel(roleid);
        if (pointControlModel > 0) {
        	pointControl = true;
        	iconId = pointControlModel;
        	LOG.info("本局为点控状态,PointControlModel =====>" + pointControlModel);
		}
        
        // 自控
        if (iconId == 0) {
        	int autoControlModel = bgManagementServiceImp.getAutoControlModel(roleid);
        	if (autoControlModel > 0) {
        		iconId = autoControlModel;
             	LOG.info("本局为自控状态,AutoControlModel =====>" + autoControlModel);
			}
		}
    	 
        // 库存控制
    	boolean InventoryControl = false; 
    	if (iconId == 0) {
    		int inventoryControlModel = bgManagementServiceImp.getInventoryControlModel(MessageIdEnum.ZCJB.getGameId(), roleid);
    		if (inventoryControlModel > 0) {
    			InventoryControl = true;
        		iconId = inventoryControlModel;
        		LOG.info("本局为库存控制状态,inventoryControlModel =====>" + inventoryControlModel);
			}
		}
    	
    	 // 系统自控
    	if (iconId == 0) {
    		int LhjSystemControlModel = bgManagementServiceImp.getLhjSystemControlModel(MessageIdEnum.ZCJB.getRedisName(), lastRoundIconId);
    		if (LhjSystemControlModel > 0) {
    			iconId = LhjSystemControlModel;
    			LOG.info("本局为系统控制状态,LhjSystemControlModel =====>" + LhjSystemControlModel);
			}
		}
        
        // 保存本局使用的图标id
    	lastRoundIconId = iconId;
		
    	 // TODO 赔率奖池功能暂时关闭
/*		// 赔率奖池奖励
		PayoutLimit queryWinlimit = bgManagementServiceImp.queryWinlimit();
		boolean isOddsPool = false;
		double oddsReward = 0;
		if (queryWinlimit != null) {
			// 查询玩家是否满足赔率奖池中奖条件
			isOddsPool = bgManagementServiceImp.queryPlayerWinlimit(roleid);
		}
		
		if (isOddsPool) {
			// 中奖概率
			double odds2 = queryWinlimit.getOdds() * 10000;
			double random = RandomUtil.getRandom(1.0, 10000.0);
			if (random > 10000.0 - odds2) {
				// 获取的赔率奖池奖励金额
				oddsReward = queryWinlimit.getPayLowLimit() * queryWinlimit.getPayRatio();
				iconId = 8;
				LOG.info("本局开奖模式为 =====> 赔率奖池奖励,使用iconId = " + 8);
			}
		}*/
		
		// 赔率奖池游戏
    	double oddsReward = 0;
		if (oddsReward > 0) {
			int count = 0;
			while (true) {
				result = gameService.doGameProcess(roleid, betInfo,iconId);
				count ++;
				if (result.getRewardcoin() >= oddsReward - oddsReward*0.1 && result.getRewardcoin() <= oddsReward
						|| (count > 200 && result.getRewardcoin() > 0)) {
					//System.out.println("本次赔率奖池的奖励为: " + result.getRewardcoin());
					break;
				}
			}
		} else { // 正常游戏
			result = gameService.doGameProcess(roleid, betInfo,iconId);
			// 验证大奖金额
			SoltGameInfo gameInfo = bgManagementServiceImp.queryGameInfo(MessageIdEnum.ZCJB.getRedisName());
			double checkAmount = gameInfo.getCheckAmount();
			if (result.getRewardcoin() >= checkAmount && checkAmount > 0) {
				boolean checkBigReward = bgManagementServiceImp.checkBigReward(gameInfo,result.getRewardcoin());
				if (!checkBigReward) {
					while (true) {
						result = gameService.doGameProcess(roleid, betInfo,iconId);
						if (result.getRewardcoin() < checkAmount) {
							break;
						}
					}
				}
			}
		}
		
		// 金币变化
		double change = NumberTool.subtract(result.getRewardcoin(), m).doubleValue();
        if (InventoryControl) {
        	bgManagementServiceImp.updateInventoryControlInfo(change, MessageIdEnum.ZCJB.getGameId());
		}
		
		//派彩
		userService.payout(roleid, result.getRewardcoin(), roundId, uuid, MessageIdEnum.ZCJB.getGameId());
		// 设置玩家金币
		result.setUsercoin(NumberTool.add(usercoin, change).doubleValue());
		
		// 记录日志
		Date date = new Date(); // 回合时间信息
		ShowRecordResult srr = buildLog(result, userinfo, startbalance, m, betInfo, false, roundId, date);
		gameService.winInfosForBgView(result,srr);
		// 游戏记录
        List<ShowRecordResult> list = Lists.newArrayList(srr);
        GameRecordDTO<ShowRecordResult> record = GameRecordDTO.<ShowRecordResult>builder()
                .gameDate(date)
                .userId(Long.parseLong(roleid))
                .userName(userinfo.getNickname())
                .gameId(MessageIdEnum.ZCJB.getBgId())
                .gameName(Constants.ZCJB_GAME_NAME)
                .fileId(1)
                .fileName("平民场")
                .gameResult(list)
                .beforeGameGold(BigDecimal.valueOf(startbalance))
                .afterGameGold(BigDecimal.valueOf(result.getUsercoin()))
                .betsGold(BigDecimal.valueOf(m))
                .winLosGold(BigDecimal.valueOf(change))
                .serviceCharge(BigDecimal.ZERO)
                .controlState(pointControl)
                .isRobot(false)
                .roundCode(RoundIdUtils.getGameRecordId(String.valueOf(MessageIdEnum.ZCJB.getBgId()), roleid))
                .build();
		
        bgManagementServiceImp.updaeInventory(record); // 更新库存
		userService.sendLog(record);
		result.setRoundId(roundId);
		LOG.info("GameResult =====> " + result);
		return result;
	}
	
	/**
	 * 构建日志
	 * @param result
	 * @param userinfo
	 * @param startbalance
	 * @param totalBet
	 * @param betInfo
	 * @param freeGame
	 * @param roundId
	 * @param date
	 * @return
	 */
	private ShowRecordResult buildLog(ZctzGameBetResult result, Player userinfo, double startbalance, double totalBet,
			BetInfo betInfo, boolean freeGame, String roundId, Date date) {
		ShowRecordResult srr = new ShowRecordResult();
		srr.setGameName(Constants.ZCJB_GAME_NAME);
		if (freeGame) {
			srr.setRecordType(Constants.RECORDTYPE2);
		}else {
			srr.setRecordType(Constants.RECORDTYPE1);
		}
		String formatDate = DateUtils.format(date, DateUtils.fp1);
		srr.setDate(formatDate);
		srr.setStartbalance(startbalance);
		double bet1 = 0;
		if (freeGame) {
			bet1 = 0;
		}else {
			bet1 = totalBet;
		}
		srr.setBet(bet1);
		srr.setBetInfo(betInfo);
		srr.setReward(result.getRewardcoin());
		srr.setEndbalance(result.getUsercoin());
		srr.setGameresult(result);
		srr.setRolenick(userinfo.getNickname());
		srr.setRoundId(roundId);
		return srr;
	}
}
