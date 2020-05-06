/**
 * 
 */
package com.zyhy.lhj_server.prcess.xywjs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.zyhy.common_lhj.Player;
import com.zyhy.common_lhj.process.AbstractHttpMsgProcess;
import com.zyhy.common_server.result.HttpMessageResult;
import com.zyhy.lhj_server.bgmanagement.config.MessageIdEnum;
import com.zyhy.lhj_server.bgmanagement.entity.SoltGameInfo;
import com.zyhy.lhj_server.bgmanagement.manager.CacheManager;
import com.zyhy.lhj_server.bgmanagement.service.imp.BgManagementServiceImp;
import com.zyhy.lhj_server.constants.MessageConstants;
import com.zyhy.lhj_server.game.xywjs.poi.impl.FruitTemplateService;
import com.zyhy.lhj_server.game.xywjs.poi.template.FruitOdds;
import com.zyhy.lhj_server.prcess.result.xywjs.XywjsLoginResult;
import com.zyhy.lhj_server.userservice.UserService;

/**
 * @author linanjun
 * 用户登录
 */
@Order
@Component
public class XywjsUserLoginProcess extends AbstractHttpMsgProcess{
	@Autowired
	private BgManagementServiceImp bgManagementServiceImp;
	@Autowired
	private UserService userService;
	@Autowired
	private FruitTemplateService templateService;
	private static final Logger LOG = LoggerFactory.getLogger(XywjsUserLoginProcess.class);
	@Override
	public int getMessageId() {
		return MessageConstants.XYWJS_USER_APPLY_LOGIN;
	}

	@Override
	public HttpMessageResult handler(String uuid, Map<String, String> body)
			throws Throwable {
		XywjsLoginResult result = new XywjsLoginResult();
		
		//获取用户信息
		String roleid = body.get("roleid");
		Player user = userService.getUserInfo(roleid, uuid);
		if(user == null){
			result.setRet(2);
			result.setMsg("登录失败");
			return result;
		}
		
		// 验证准入金额
		SoltGameInfo gameInfo = bgManagementServiceImp.queryGameInfo(MessageIdEnum.XYWJS.getRedisName());
		if (user.getGold() < gameInfo.getInAmount()) {
			result.setRet(4);
			result.setMsg("金币达不到入场要求");
			result.setInAmount(gameInfo.getInAmount());
			result.setCurrentGold(user.getGold());
			return result;
		}
		
		// 获取当前汇率
		double exchangeRate = CacheManager.instance().getExchangeRate(); // 当前汇率
		LOG.info("当前游戏汇率 =====> " + exchangeRate);
		if (exchangeRate <= 0d) {
			result.setRet(2);
			result.setMsg("exchangeRate error!");
			return result;
		}
		
		// 获取图标赔率
		Map<Integer, Integer> oddsMap = new HashMap<>(); // 图标赔率
		List<FruitOdds> oddsList = new ArrayList<>();
		List<FruitOdds> baseinfos = templateService.getList(FruitOdds.class);
		for (FruitOdds fruitOdds : baseinfos) {
			if (fruitOdds.getType() == 1) {
				oddsList.add(fruitOdds);
			}
		}
		for (FruitOdds fo : oddsList) {
			oddsMap.put(fo.getSign(), fo.getMultiple());
		}
		
		if (oddsMap.size() == 0) {
			result.setRet(2);
			result.setMsg("oddsMap error!");
			return result;
		}
		
		result.setOddsMap(oddsMap);
		result.setExchangeRate(exchangeRate);
		result.setSid(1);
		result.setGold(user.getGold());
		result.setPlayer(user);
		return result;
	}
}
