/**
 * 
 */
package com.zyhy.lhj_server.prcess.gsgl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.zyhy.common_lhj.Bet;
import com.zyhy.common_lhj.BetLimitInfo;
import com.zyhy.common_lhj.Icon;
import com.zyhy.common_lhj.IconInfo;
import com.zyhy.common_lhj.Window;
import com.zyhy.common_lhj.WindowInfo;
import com.zyhy.common_lhj.process.AbstractHttpMsgProcess;
import com.zyhy.common_server.result.HttpMessageResult;
import com.zyhy.lhj_server.bgmanagement.config.MessageIdEnum;
import com.zyhy.lhj_server.bgmanagement.service.imp.BgManagementServiceImp;
import com.zyhy.lhj_server.constants.MessageConstants;
import com.zyhy.lhj_server.game.gsgl.GsglBetEnum;
import com.zyhy.lhj_server.game.gsgl.GsglBonusInfo;
import com.zyhy.lhj_server.game.gsgl.GsglIconEnum;
import com.zyhy.lhj_server.game.gsgl.GsglWindowEnum;
import com.zyhy.lhj_server.prcess.result.gsgl.GsglGamePanlResult;
import com.zyhy.lhj_server.service.gsgl.GsglBonusService;

/**
 * @author linanjun
 * 打开游戏页面
 */
@Order
@Component
public class GsglOpenGamePanlProcess extends AbstractHttpMsgProcess{
	@Autowired
	private BgManagementServiceImp bgManagementServiceImp;
	public Map<Double, Integer> betenum = new HashMap<>();
	@Autowired
	private GsglBonusService bonusService;
	@Autowired
	private StringRedisTemplate redisTemplate;
	@Override
	public int getMessageId() {
		return MessageConstants.GSGL_OPEN_GAME_PANL;
	}

	@Override
	public HttpMessageResult handler(String uuid, Map<String, String> body)
			throws Throwable {
		String roleid = body.get("roleid");
		GsglGamePanlResult result = new GsglGamePanlResult();
		
		// 获取后台管理赌注配置
		List<Double> betList = bgManagementServiceImp.queryBetList(MessageIdEnum.JSGL.getRedisName());
		for (int i = 0; i < betList.size(); i++) {
			for (GsglBetEnum e : GsglBetEnum.values()) {
				if (e.getId() == i) {
					e.setBetcoin(betList.get(i));
					betenum.put(e.getBetcoin(), e.getId());
				}
			}
		}
		
		result.setBetlimitinfos(betinfos(ArrayUtils.subarray(GsglBetEnum.values(), 0,betList.size())));
		result.setWindowinfos(windowinfos(GsglWindowEnum.values()));
		result.setZooIconInfos(iconinfos(GsglIconEnum.values()));
		
		GsglBonusInfo bi = bonusService.getData(roleid, uuid);
		if (bi != null) {
			double lastbet = bi.getBetInfo().getGold();
			if (betenum.containsKey(lastbet)) {
				bi.setBetLine( betenum.get(lastbet));
			} else {
				betenum.put(lastbet, betenum.size());
				bi.setBetLine( betenum.size());
			}
			result.setBonusInfo(bi);
		}
		
		/*String v = redisTemplate.opsForValue().get(Constants.GSGL_REDIS_LHJ_GSGL_USERBET + roleid);
		if(v != null){
			result.setBetInfo(JSONObject.parseObject(v, BetInfo.class));
		}*/
		
		return result;
	}
	
	private List<BetLimitInfo> betinfos(Bet[] bs) {
		List<BetLimitInfo> is = new ArrayList<BetLimitInfo>();
		for(Bet b : bs) {
			is.add(new BetLimitInfo(b));
		}
		return is;
	}
	
	private List<WindowInfo> windowinfos(Window[] ws){
		List<WindowInfo> ls = new ArrayList<WindowInfo>();
		for(Window t : ws) {
			ls.add(new WindowInfo(t));
		}
		return ls;
	}
	
	private List<IconInfo> iconinfos(Icon[] is) {
		List<IconInfo> ls = new ArrayList<IconInfo>();
		for(Icon i : is) {
			ls.add(new IconInfo(i));
		}
		return ls;
	}

}
