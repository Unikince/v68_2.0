/**
 * 
 */
package com.zyhy.lhj_server.service.gsgl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zyhy.common_lhj.BetInfo;
import com.zyhy.common_lhj.Icon;
import com.zyhy.common_lhj.Line;
import com.zyhy.common_lhj.WinLineInfo;
import com.zyhy.common_lhj.Window;
import com.zyhy.common_lhj.logic.BaseLogic;
import com.zyhy.lhj_server.game.gsgl.GsglIconEnum;
import com.zyhy.lhj_server.game.gsgl.GsglRollerWeightEnum;
import com.zyhy.lhj_server.game.gsgl.GsglWinLineEnum;
import com.zyhy.lhj_server.game.gsgl.poi.impl.GsglTemplateService;
import com.zyhy.lhj_server.game.gsgl.poi.template.GsglOdds;
import com.zyhy.lhj_server.prcess.result.gsgl.GsglGameBetResult;

/**
 * @author linanjun
 *
 */
@Service
public class GsglGameService extends BaseLogic{
	@Autowired
	private GsglBonusService bonusService;
	@Autowired
	private GsglTemplateService templateService;
	/**
	 * 
	 * @param iconId 
	 * @param jetton 档位
	 * @param betnum 下注的线路数
	 * @return
	 */
	public GsglGameBetResult doGameProcess(String roleid, BetInfo betinfo, int iconId) {
		// 获取图标
		List<GsglOdds> baseinfos = templateService.getList(GsglOdds.class);
		//System.out.println("加载进来的图标有:" + baseinfos);
		
		// 获取指定赔率图标
		Iterator<GsglOdds> iterator = baseinfos.iterator();
		while (iterator.hasNext()) {
			GsglOdds next = iterator.next();
			if (next.getType() != iconId) {
				iterator.remove();
			}
		}
		//System.out.println("加载进来的图标有:" + baseinfos);
		
		GsglGameBetResult res = new GsglGameBetResult();
		//窗口信息
		List<Window> ws = new ArrayList<Window>();
		for(int i=1; i<=5; i++){
			ws.addAll(GsglRollerWeightEnum.windowInfo(i,baseinfos));
		}
		res.setWindowinfos(ws);
		//获胜线路信息
		List<WinLineInfo> wins = getWininfo(ws, betinfo);
		//scatter奖励
		WinLineInfo scatterwin = getScatterWininfo(ws, betinfo);
		
		if(scatterwin != null){
			wins.add(scatterwin);
		}
		//计算总奖励
		if(wins != null && wins.size() > 0){
			//总的奖励数
			BigDecimal reward = new BigDecimal(0);
			for(WinLineInfo w : wins){
				reward = reward.add(new BigDecimal(w.getReward() + ""));
			}
			res.setRewardcoin(reward.doubleValue());
		}
		res.setWinrouteinfos(wins);
		
		//bonus小游戏是否触发
		List<Window> bonuswins = bonusService.getBonusWindows(ws);
		if(bonuswins != null){
			WinLineInfo wl = new WinLineInfo();
			wl.setId("Bonus");
			wl.setWindows(bonuswins);
			wl.setIcon(GsglIconEnum.BONUS);
			res.getWinrouteinfos().add(wl);
			res.setBonus(1);
		}
		return res;
	}

	/**
	 * scatter奖励信息
	 * @param ws
	 * @param betinfo
	 * @return
	 */
	private WinLineInfo getScatterWininfo(List<Window> ws, BetInfo betinfo) {
		//个数
		int n = 0;
		for(Window w : ws){
			if(w.getIcon().isScatter()){
				n++;
			}
		}
		if(n > 0){
			double[] trs = GsglIconEnum.SCATTER.getTrs();
			if(n > trs.length){
				n = trs.length;
			}
			double tr = trs[n-1];
			if(tr > 0){
				WinLineInfo winfo = new WinLineInfo();
				winfo.setId("Scatter");
				winfo.setReward(tr);
				winfo.setNum(n);
				winfo.setIcon(GsglIconEnum.SCATTER);
				reward(winfo, betinfo);
				return winfo;
			}
		}
		return null;
	}

	/**
	 * 得到获胜线路信息
	 * @param ws
	 * @param betnum 线路数
	 * @return
	 */
	private List<WinLineInfo> getWininfo(List<Window> ws, BetInfo betinfo) {
		List<WinLineInfo> res = new ArrayList<WinLineInfo>();
		//获取所有线路
		Line[] ts = ArrayUtils.subarray(GsglWinLineEnum.values(), 0, betinfo.getNum());
		for(Line line : ts){
			List<Window> lws = getLineWindows(ws, line);
			//正面
			WinLineInfo wl = getWinLineInfo(lws);
			if(wl != null && !wl.getIcon().isScatter()){
				wl.setOrder(1);
				wl.setId(String.valueOf(line.getId()));
				reward(wl, betinfo);
				res.add(wl);
			}
			//5个连续时，不再获得反面奖励
			if(wl != null && wl.getNum() == 5){
				continue;
			}
			//反面
			List<Window> lwsreverse = new ArrayList<Window>(lws);
			Collections.reverse(lwsreverse);
			WinLineInfo wl2 = getWinLineInfo(lwsreverse);
			if(wl2 != null && !wl2.getIcon().isScatter()){
				wl2.setOrder(2);
				wl2.setId(String.valueOf(line.getId()));
				reward(wl2, betinfo);
				res.add(wl2);
			}
		}
		return res;
	}
	
	@Override
	public WinLineInfo getWinLineInfo(List<Window> lineWindows) {
		int wail = 0;
		int num = 0;
		//是否scatter触发免费游戏
		Icon lineIcon = null;
		int size = lineWindows.size();
		for(int i=0;i<size;i++){
			Icon icon = lineWindows.get(i).getIcon();
			if(i > 0 && i != wail && !icon.isWild() && lineIcon != icon){
				break;
			}
			if(icon.isScatter()){
				lineIcon = icon;
			}else if(icon.isWild()){
				wail++;
			}else if(icon.isBar()){
				lineIcon = icon;
			}else{
				lineIcon = icon;
			}
			num++;
		}
		if(num > 0){
			if(lineIcon == null){
				lineIcon = lineWindows.get(0).getIcon();
			}
			double v = lineIcon.getTrs()[num-1];
			if(v > 0){
				WinLineInfo winfo = new WinLineInfo();
				winfo.setReward(v);
				winfo.setNum(num);
				winfo.setIcon(lineIcon);
				winfo.setWindows(new ArrayList<Window>(lineWindows.subList(0, num)));
				return winfo;
			}
		}
		return null;
	}

	@Override
	public void rewardWail(WinLineInfo wl, BetInfo betinfo) {
		return;
	}
}
