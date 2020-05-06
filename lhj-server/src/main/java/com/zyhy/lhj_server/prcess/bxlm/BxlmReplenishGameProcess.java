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
 * @author DPC
 * @version 创建时间：2019年2月26日 下午5:04:42
 * 
 * 补充游戏数据
 */
@Order
@Component
public class BxlmReplenishGameProcess extends AbstractHttpMsgProcess {
	@Autowired
	private BgManagementServiceImp bgManagementServiceImp;
	@Autowired
	private BxlmGameService gameService;
	@Autowired
	private BxlmReplenishService replenishService;
	@Autowired
	private UserService userService;
	private static final Logger LOG = LoggerFactory.getLogger(BxlmReplenishGameProcess.class);
	
	@Override
	public int getMessageId() {
		return MessageConstants.BXLM_REPLENISH_GAME_INFO;
	}

	@Override
	public HttpMessageResult handler(String uuid, Map<String, String> body)
			throws Throwable {
		String roleid = body.get("roleid");
		BxlmGameBetResult result = new BxlmGameBetResult();
		
		// 检查掉落数据
		BxlmReplenish rep = replenishService.getBxlmReplenishData(roleid, uuid);
		if(rep == null || !rep.isBxlmReplenish()){
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
		// 下注信息
		BetInfo betInfo = rep.getBetInfo();
		// 回合id
		String roundId = MessageIdEnum.BXQY.getGameId() + "|" + userService.getOrderId();
		
		/*// 免费游戏下注
		boolean betResult = userService.bet(roleid, 0, roundId, uuid, Constants.BETTYPE0,Constants.BXQY_GAME_ID);
		if (!betResult) {
			result.setRet(2);
			result.setMsg("下注失败");
			return result;
		}*/
		
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
    		int LhjSystemControlModel = bgManagementServiceImp.getLhjSystemControlModel(MessageIdEnum.BXQY.getRedisName(), gameService.lastRoundIconId);
    		if (LhjSystemControlModel > 0) {
    			iconId = LhjSystemControlModel;
    			LOG.info("本局为系统控制状态,LhjSystemControlModel =====>" + LhjSystemControlModel);
			}
		}
		
    	// 正常游戏
		result = replenishService.doGameProcess(roleid, rep,iconId);
		// 验证大奖金额
		SoltGameInfo gameInfo = bgManagementServiceImp.queryGameInfo(MessageIdEnum.BXQY.getRedisName());
		double checkAmount = gameInfo.getCheckAmount();
		if (result.getRewardcoin() >= checkAmount && checkAmount > 0) {
			boolean checkBigReward = bgManagementServiceImp.checkBigReward(gameInfo,result.getRewardcoin());
			if (!checkBigReward) {
				while (true) {
					result = replenishService.doGameProcess(roleid, rep,iconId);
					if (result.getRewardcoin() < checkAmount) {
						break;
					}
				}
			}
		}
		
		//金币变化
		double change = NumberTool.subtract(result.getRewardcoin(), 0.0).doubleValue();
	    // 如果是库存控制,就更新本局金币变化
        if (InventoryControl) {
        	bgManagementServiceImp.updateInventoryControlInfo(change, MessageIdEnum.BXQY.getGameId());
		}
		
		//更新免费游戏总奖励
		BxlmScatterInfo bi = gameService.getData(roleid, uuid);
		if(bi != null){
			bi.setGold(bi.getGold() + result.getRewardcoin());
			gameService.save(bi, roleid, userinfo, uuid);
			result.setScatterNum(bi.getNum());
			result.setScatterInfo(bi);
		}
		
		// 删除免费信息
		if(bi != null && bi.getNum() == 0 ){
			gameService.deleteInfo(roleid, uuid);
		}
		
		// 补充数据
		if(result.isReplenish()){
			replenishService.save(result.getRep(),roleid, uuid);
		} else {
			replenishService.delete(roleid, uuid);
		}
		
		// 派彩
		userService.payout(roleid, result.getRewardcoin(), roundId, uuid, MessageIdEnum.BXQY.getBgId());
		// 设置玩家金币
		result.setUsercoin(NumberTool.add(usercoin, change).doubleValue());
		
		// 记录日志
		Date date = new Date(); // 回合时间信息
		ShowRecordResult srr = buildLog(result, userinfo, startbalance, betInfo.getTotalBet(), betInfo, true, roundId, date);
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
                .betsGold(BigDecimal.valueOf(betInfo.getTotalBet()))
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

}
