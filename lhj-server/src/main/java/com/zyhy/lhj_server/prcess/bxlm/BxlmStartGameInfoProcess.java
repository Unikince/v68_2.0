/**
 * 
 */
package com.zyhy.lhj_server.prcess.bxlm;

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
import com.zyhy.lhj_server.game.bxlm.BxlmReplenish;
import com.zyhy.lhj_server.game.bxlm.BxlmScatterInfo;
import com.zyhy.lhj_server.prcess.result.bxlm.BxlmGameBetResult;
import com.zyhy.lhj_server.service.bxlm.BxlmGameService;
import com.zyhy.lhj_server.service.bxlm.BxlmReplenishService;
import com.zyhy.lhj_server.userservice.UserService;

/**
 * @author linanjun
 * 开始游戏
 */
@Order
@Component
public class BxlmStartGameInfoProcess extends AbstractHttpMsgProcess{
	@Autowired
	private BgManagementServiceImp bgManagementServiceImp;
	@Autowired
	private BxlmOpenGamePanlProcess openGamePanlProcess;
	@Autowired
	private BxlmGameService gameService;
	@Autowired
	private BxlmReplenishService replenishService;
	@Autowired
	private UserService userService;
	// 上一局使用图标序列id
	private int lastRoundIconId = 0; 
	private static final Logger LOG = LoggerFactory.getLogger(BxlmStartGameInfoProcess.class);
	
	@Override
	public int getMessageId() {
		return MessageConstants.BXLM_START_GAME_INFO;
	}

	@Override
	public HttpMessageResult handler(String uuid, Map<String, String> body) throws Throwable {
		// 接收的信息
		String roleid = body.get("roleid");
		double lineBet = Double.parseDouble(body.get("jetton"));//档位
		int betnum = Integer.parseInt(body.get("num"));//银币数
		// 返回信息
		BxlmGameBetResult result = new BxlmGameBetResult();
		
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
		
		if(!betEnum || betnum < 1 || betnum > 10){
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
		BxlmReplenish rep = replenishService.getBxlmReplenishData(roleid, uuid);
		if(rep != null && rep.isBxlmReplenish()){
			result.setRet(2);
			result.setMsg("掉落中");
			return result;
		}
		
		// 玩家游戏币
		double usercoin = userinfo.getGold();
		// 投注前金币
		double startbalance = usercoin;
		//总投注(线注*硬币*30)
		double totalBet = new BigDecimal( lineBet*betnum*30).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		
		// 检查游戏币合理性
		if (usercoin < totalBet) {
			result.setRet(4);
			result.setMsg("游戏币不足");
			return result;
		}
		BetInfo betInfo = new BetInfo(lineBet, betnum ,totalBet);
		
		//判断是否免费游戏中
		BxlmScatterInfo bi = gameService.getData(roleid, uuid);
		boolean freeGame = false;
		if(bi != null && bi.getNum() > 0){//正在进行免费游戏
			betInfo = bi.getBetInfo();
			freeGame = true;
		}
		
		if(bi != null && bi.getModel() == 0){//正在进行免费游戏
			result.setRet(2);
			result.setMsg("免费游戏模式选择");
			return result;
		}
		
		// 回合id
		String roundId = MessageIdEnum.BXQY.getGameId() + "|" + userService.getOrderId();
		
		//下注
		if(!freeGame){
			//正常游戏下注
			boolean betResult = userService.bet(roleid, totalBet, roundId, uuid, Constants.BETTYPE1,MessageIdEnum.BXQY.getBgId());
			if (!betResult) {
				result.setRet(2);
				result.setMsg("下注失败");
				return result;
			}
		}else {
			//免费游戏扣除次数
			bi.setNum(bi.getNum() - 1);
			/*// 免费游戏下注
			boolean betResult = userService.bet(roleid, 0, roundId, uuid, Constants.BETTYPE0,Constants.BXQY_GAME_ID);
			if (!betResult) {
				result.setRet(2);
				result.setMsg("下注失败");
				return result;
			}*/
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
    		int inventoryControlModel = bgManagementServiceImp.getInventoryControlModel(MessageIdEnum.BXQY.getGameId(), roleid);
    		if (inventoryControlModel > 0) {
    			InventoryControl = true;
        		iconId = inventoryControlModel;
        		LOG.info("本局为库存控制状态,inventoryControlModel =====>" + inventoryControlModel);
			}
		}
    	
    	 // 系统自控
    	if (iconId == 0) {
    		int LhjSystemControlModel = bgManagementServiceImp.getLhjSystemControlModel(MessageIdEnum.BXQY.getRedisName(), lastRoundIconId);
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
				result = gameService.doGameProcess(roleid, betInfo,freeGame,bi,iconId);
				count ++;
				if (result.getRewardcoin() >= oddsReward - oddsReward*0.1 && result.getRewardcoin() <= oddsReward
						|| (count > 200 && result.getRewardcoin() > 0)) {
					break;
				}
			}
		} else { // 正常游戏
			result = gameService.doGameProcess(roleid, betInfo,freeGame,bi,iconId);
			// 验证大奖金额
			SoltGameInfo gameInfo = bgManagementServiceImp.queryGameInfo(MessageIdEnum.BXQY.getRedisName());
			double checkAmount = gameInfo.getCheckAmount();
			if (result.getRewardcoin() >= checkAmount && checkAmount > 0) {
				boolean checkBigReward = bgManagementServiceImp.checkBigReward(gameInfo,result.getRewardcoin());
				if (!checkBigReward) {
					while (true) {
						result = gameService.doGameProcess(roleid, betInfo,freeGame,bi,iconId);
						if (result.getRewardcoin() < checkAmount) {
							break;
						}
					}
				}
			}
		}
		
		// 保存补充数据
		if(result.isReplenish()){
			BxlmReplenish newRep = result.getRep();
			replenishService.save(newRep, roleid, uuid);
		}
				
		//金币变化
		double change = NumberTool.subtract(result.getRewardcoin(), totalBet).doubleValue();
        // 如果是库存控制,就更新本局金币变化
        if (InventoryControl) {
        	bgManagementServiceImp.updateInventoryControlInfo(change, MessageIdEnum.BXQY.getGameId());
		}
		
		//更新免费游戏总奖励
		if(freeGame ){
			change = NumberTool.subtract(result.getRewardcoin(), 0.0).doubleValue();
			bi.setGold(NumberTool.add(bi.getGold(), result.getRewardcoin()).doubleValue());
			gameService.save(bi, roleid, userinfo, uuid);
			result.setScatterInfo(bi);
			result.setScatterNum(bi.getNum());
		}
		
		//是否要进行免费游戏
		if(result.isScatter() && !freeGame){
			BxlmScatterInfo sinfo = new BxlmScatterInfo(betInfo);
			sinfo.setGold(0);
			sinfo.setModel(0);
			result.setScatterNum(0);
			result.setScatterInfo(sinfo);
			gameService.save(sinfo, roleid, userinfo, uuid);
		}
		
		// 清除掉落信息
		if(bi != null && bi.getNum() == 0 && !result.isReplenish()){
			gameService.deleteInfo(roleid, uuid);
		}
		
		// 派彩
		if(!freeGame){
			userService.payout(roleid, result.getRewardcoin(), roundId, uuid,MessageIdEnum.BXQY.getBgId());
		}else {
			userService.payout(roleid, result.getRewardcoin(), roundId, uuid,MessageIdEnum.BXQY.getBgId());
		}
		// 设置玩家金币
		result.setUsercoin(NumberTool.add(usercoin, change).doubleValue());
		
		// 记录日志
		Date date = new Date(); // 回合时间信息
		ShowRecordResult srr = buildLog(result, userinfo, startbalance, totalBet, betInfo, freeGame, roundId, date);
		gameService.winInfosForBgView(result,srr);
		// 游戏记录
        List<ShowRecordResult> list = Lists.newArrayList(srr);
        GameRecordDTO<ShowRecordResult> record = GameRecordDTO.<ShowRecordResult>builder()
                .gameDate(date)
                .userId(Long.parseLong(roleid))
                .userName(userinfo.getNickname())
                .gameId(MessageIdEnum.BXQY.getBgId())
                .gameName(Constants.BXQY_GAME_NAME)
                .fileId(1)
                .fileName("平民场")
                .gameResult(list)
                .beforeGameGold(BigDecimal.valueOf(startbalance))
                .afterGameGold(BigDecimal.valueOf(result.getUsercoin()))
                .betsGold(BigDecimal.valueOf(totalBet))
                .winLosGold(BigDecimal.valueOf(change))
                .serviceCharge(BigDecimal.ZERO)
                .controlState(pointControl)
                .isRobot(false)
                .roundCode(RoundIdUtils.getGameRecordId(String.valueOf(MessageIdEnum.BXQY.getBgId()), roleid))
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
	private ShowRecordResult buildLog(BxlmGameBetResult result, Player userinfo, double startbalance, double totalBet,
			BetInfo betInfo, boolean freeGame, String roundId, Date date) {
		ShowRecordResult srr = new ShowRecordResult();
		srr.setGameName(Constants.BXQY_GAME_NAME);
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

	public int getLastRoundIconId() {
		return lastRoundIconId;
	}
	
}
